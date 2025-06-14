package io.tuttut.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {
     private fun <T> invokeStateFlow(state : StateFlow<T>, collect : (T)->Unit) {
        viewModelScope.launch {
            state.collect{
                collect(it)
            }
        }
    }

    private fun invokeBooleanFlow(state : StateFlow<Boolean>, onFalse : ()->Unit = {}, onTrue : ()->Unit) {
        invokeStateFlow(state){
            if(it)
                onTrue()
            else
                onFalse()
        }
    }

    fun useFlag(state : MutableStateFlow<Boolean>, onFlag : ()->Unit){
        invokeBooleanFlow(state) {
            onFlag()
            state.value = false
        }
    }
}