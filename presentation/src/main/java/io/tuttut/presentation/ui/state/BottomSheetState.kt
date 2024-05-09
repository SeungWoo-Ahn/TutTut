package io.tuttut.presentation.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface IBottomSheetState {
    var showSheet: Boolean
    fun show()
    fun dismiss()
}

open class BottomSheetState : IBottomSheetState {
    override var showSheet by mutableStateOf(false)

    override fun show() {
        showSheet = true
    }

    override fun dismiss() {
        showSheet = false
    }
}