package io.tuttut.presentation.model

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.tuttut.domain.model.user.JoinRequest
import io.tuttut.presentation.BuildConfig
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuth @Inject constructor() {
    private lateinit var credentialManager: CredentialManager

    suspend fun login(context: Context): Result<JoinRequest> = runCatching {
        val credential = getCredential(context)
        val fireAuthCredential = GoogleAuthProvider.getCredential(credential.idToken, null)
        val googleUser = Firebase.auth.signInWithCredential(fireAuthCredential).await().user
            ?: throw GoogleAuthException.UserNotFound()
        JoinRequest(
            id = googleUser.uid,
            name = googleUser.displayName ?: "텃텃 유저",
            imageUrl = googleUser.photoUrl?.toString()
        )
    }

    private suspend fun getCredential(context: Context): GoogleIdTokenCredential {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(BuildConfig.WEB_CLIENT_KEY)
            .setFilterByAuthorizedAccounts(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        credentialManager = CredentialManager.create(context)
        val credential = credentialManager.getCredential(context, request).credential
        if (credential !is CustomCredential || credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            throw GoogleAuthException.DifferentCredential()
        }
        return GoogleIdTokenCredential.createFrom(credential.data)
    }

    suspend fun logout(): Result<Unit> = runCatching {
        val clearRequest = ClearCredentialStateRequest()
        credentialManager.clearCredentialState(clearRequest)
            .also { Firebase.auth.signOut() }
    }
}

sealed class GoogleAuthException : Exception() {
    class DifferentCredential : GoogleAuthException()
    class UserNotFound : GoogleAuthException()
}