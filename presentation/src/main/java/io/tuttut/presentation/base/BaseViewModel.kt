package io.tuttut.presentation.base

import androidx.lifecycle.ViewModel
import io.tuttut.presentation.model.GoogleAuthClient
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {

    @Inject lateinit var authClient: GoogleAuthClient

}