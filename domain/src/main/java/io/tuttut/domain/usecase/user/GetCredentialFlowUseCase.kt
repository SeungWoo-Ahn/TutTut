package io.tuttut.domain.usecase.user

import io.tuttut.domain.model.user.Credential
import io.tuttut.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCredentialFlowUseCase @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
) {
    operator fun invoke(): Flow<Credential> = preferenceRepository.getCredentialFlow()
}