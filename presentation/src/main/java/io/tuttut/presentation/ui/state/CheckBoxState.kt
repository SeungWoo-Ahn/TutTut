package io.tuttut.presentation.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface ICheckBoxState {
    var checked: Boolean
    fun onCheckedChange(state: Boolean)
}

class CheckBoxState(
    initState: Boolean = false
) : ICheckBoxState {
    override var checked by mutableStateOf(initState)

    override fun onCheckedChange(state: Boolean) {
        checked = state
    }
}