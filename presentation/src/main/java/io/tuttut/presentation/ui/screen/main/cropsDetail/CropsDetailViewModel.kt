package io.tuttut.presentation.ui.screen.main.cropsDetail

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Diary
import io.tuttut.data.model.response.Result
import io.tuttut.data.repository.crops.CropsRepository
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.data.repository.diary.DiaryRepository
import io.tuttut.data.repository.garden.GardenRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.model.DiaryModel
import io.tuttut.presentation.model.PreferenceUtil
import io.tuttut.presentation.ui.state.DialogState
import io.tuttut.presentation.util.getToday
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CropsDetailViewModel @Inject constructor(
    private val cropsRepo: CropsRepository,
    gardenRepo: GardenRepository,
    cropsInfoRepo: CropsInfoRepository,
    diaryRepo: DiaryRepository,
    private val cropsModel: CropsModel,
    private val diaryModel: DiaryModel,
    private val pref: PreferenceUtil
): BaseViewModel() {
    private val crops = cropsModel.observedCrops.value
    val gardenMemberMap = gardenRepo.gardenMemberMap
    val cropsInfoMap = cropsInfoRepo.cropsInfoMap

    val uiState: StateFlow<CropsDetailUiState>
        = cropsRepo.getCropsDetail(
            gardenId = pref.gardenId,
            cropsId = crops.id
        ).map(CropsDetailUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CropsDetailUiState.Loading
        )

    val diaryUiState: StateFlow<CropsDiaryUiState>
        = diaryRepo.getDiaryList(
            gardenId = pref.gardenId,
            cropsId = crops.id
        ).take(4).map(CropsDiaryUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CropsDiaryUiState.Loading
        )

    val recipeUiState: StateFlow<CropsRecipeUiState>
        = cropsInfoRepo
            .getCropsRecipes(crops.name)
            .map(CropsRecipeUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CropsRecipeUiState.Loading
            )

    val deleteDialogState = DialogState()
    val harvestDialogState = DialogState()

    fun onMoveCropsInfo(moveCropsInfo: () -> Unit) {
        cropsModel.selectCropsInfo(cropsInfoMap[crops.key] ?: CropsInfo(), true)
        moveCropsInfo()
    }

    fun onRecipe(link: String, moveRecipeWeb: () -> Unit) {
        cropsModel.setRecipeLink(link)
        moveRecipeWeb()
    }

    fun onDiary(diary: Diary, moveDiaryDetail: () -> Unit) {
        diaryModel.observeDiary(diary)
        moveDiaryDetail()
    }


    fun onAddDiary(moveAddDiary: () -> Unit) {
        diaryModel.observeDiary(Diary())
        moveAddDiary()
    }

    fun onEdit(moveEditCrops: () -> Unit) {
        cropsModel.observeCrops(crops, true)
        moveEditCrops()
    }

    fun onHarvest(onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            cropsRepo.harvestCrops(pref.gardenId, crops.id, crops.harvestCnt).collect {
                when (it) {
                    is Result.Success -> {
                        harvestDialogState.dismiss()
                        onShowSnackBar("${crops.nickName}을/를 수확했어요", null)
                    }
                    is Result.Error -> onShowSnackBar("수확을 실패했어요", null)
                    else -> {}
                }
            }
        }
    }

    fun onWatering(onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            if (crops.wateringInterval == null) {
                onShowSnackBar("물 주기 간격을 설정해주세요", null)
            }
            else if (crops.lastWatered == getToday()) {
                onShowSnackBar("오늘 물을 줬어요", null)
            } else {
                cropsRepo.wateringCrops(
                    gardenId = pref.gardenId,
                    cropsId = crops.id,
                    today = getToday()
                ).collect {
                    when (it) {
                        is Result.Success -> onShowSnackBar("${crops.nickName}에 물을 줬어요", null)
                        is Result.Error -> onShowSnackBar("물주기에 실패했어요", null)
                        else -> {}
                    }
                }
            }
        }
    }

    fun onDelete(moveMain: () -> Unit, onShowSnackBar: suspend (String, String?) -> Boolean) {
        viewModelScope.launch {
            cropsRepo.deleteCrops(pref.gardenId, crops.id).collect {
                when (it) {
                    is Result.Success -> {
                        deleteDialogState.dismiss()
                        moveMain()
                        onShowSnackBar("${crops.nickName}을/를 삭제했어요", null)
                    }
                    is Result.Error -> onShowSnackBar("삭제에 실패했어요", null)
                    else -> {}
                }
            }
        }
    }
}