package io.tuttut.presentation.ui.screen.main.selectCrops

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.CUSTOM_KEY
import io.tuttut.data.model.dto.Crops
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import io.tuttut.presentation.util.getToday
import javax.inject.Inject

@HiltViewModel
class SelectCropsViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
    private val cropsModel: CropsModel
): BaseViewModel() {
    fun onItemClick(item: CropsInfo, moveDetail: () -> Unit) {
        cropsModel.selectCropsInfo(item, false)
        moveDetail()
    }

    fun onButton(moveAdd: () -> Unit) {
        cropsModel.setCropsState(Crops(key = CUSTOM_KEY, plantingDate = getToday()))
        moveAdd()
    }
}