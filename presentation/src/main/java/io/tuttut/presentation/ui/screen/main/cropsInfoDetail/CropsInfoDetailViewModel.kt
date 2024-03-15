package io.tuttut.presentation.ui.screen.main.cropsInfoDetail

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.util.getToday
import javax.inject.Inject

@HiltViewModel
class CropsInfoDetailViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
    private val cropsModel: CropsModel,
): BaseViewModel() {
    val cropsInfo = cropsModel.selectedCropsInfo

    fun onButton(moveAdd: () -> Unit) {
        cropsInfo.value.let {
            cropsModel.setCropsState(
                Crops(
                    key = it.key,
                    name = it.name,
                    wateringInterval = it.wateringInterval,
                    growingDay = it.growingDay,
                    plantingDate = getToday()
                )
            )
        }
        moveAdd()
    }
}