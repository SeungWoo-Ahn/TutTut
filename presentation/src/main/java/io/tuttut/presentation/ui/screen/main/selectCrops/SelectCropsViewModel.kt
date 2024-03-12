package io.tuttut.presentation.ui.screen.main.selectCrops

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.repository.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SelectCropsViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository
): BaseViewModel() {
    fun onItemClick(item: CropsInfo, moveDetail: () -> Unit) {
        cropsInfoRepo.selectedCropsInfo.value = item
        moveDetail()
    }

    fun onButton(moveAdd: () -> Unit) {
        cropsInfoRepo.selectedCropsInfo.value = CropsInfo()
        moveAdd()
    }
}