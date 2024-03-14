package io.tuttut.presentation.ui.screen.admin

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.tuttut.data.model.dto.CropsInfo
import io.tuttut.data.model.dto.Difficulty
import io.tuttut.data.model.dto.Response
import io.tuttut.data.model.dto.Season
import io.tuttut.data.repository.CropsInfoRepository
import io.tuttut.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
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