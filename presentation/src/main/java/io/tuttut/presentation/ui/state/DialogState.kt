package io.tuttut.presentation.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface IDialogState {
    var isOpen: Boolean
    fun show()
    fun dismiss()
}


open class DialogState(
    initState: Boolean = false
): IDialogState {
    override var isOpen by mutableStateOf(initState)

    override fun show() {
        isOpen = true
    }

    override fun dismiss() {
        isOpen = false
    }
}