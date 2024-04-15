package io.tuttut.presentation.model

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserModel @Inject constructor() {
    val refreshMember = MutableStateFlow(false)

    fun refreshMember() {
        refreshMember.value = true
    }
}