package io.tuttut.presentation.ui.screen.main.cropsInfoDetail

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import io.tuttut.presentation.model.CropsModel
import javax.inject.Inject

@HiltViewModel
class CropsInfoDetailViewModel @Inject constructor(
    val cropsInfoRepo: CropsInfoRepository,
    cropsModel: CropsModel,
): BaseViewModel() {
    val cropsInfo = cropsModel.selectedCropsInfo
}