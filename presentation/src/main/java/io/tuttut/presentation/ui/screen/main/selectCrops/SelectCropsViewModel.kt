package io.tuttut.presentation.ui.screen.main.selectCrops

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import javax.inject.Inject

@HiltViewModel
class SelectCropsViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
    private val cropsModel: CropsModel
): BaseViewModel() {
    fun onItemClick(item: CropsInfo, moveDetail: () -> Unit) {
        cropsModel.setAddCropsState(item)
        moveDetail()
    }

    fun onButton(moveAdd: () -> Unit) {
        cropsModel.setAddCropsState()
        moveAdd()
    }
}