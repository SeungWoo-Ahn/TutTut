package io.tuttut.data.network.model

import com.google.firebase.firestore.DocumentId

data class GardenDto(
    @DocumentId
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val created: String = "",
    val groupIdList: List<String> = listOf(),
)
