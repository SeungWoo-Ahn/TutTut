package io.tuttut.presentation.ui.screen.admin

import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.repository.cropsInfo.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val cropsInfoRepo: CropsInfoRepository
): BaseViewModel() {
    fun onClick() {
/*        viewModelScope.launch {
            val result = cropsInfoRepo.addCropsInfoByAdmin(cropsInfo)
            if (result is Response.Success) {
                Log.d("Admin", "onClick: 추가 성공")
            }
        }*/
    }
}