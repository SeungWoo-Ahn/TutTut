package io.tuttut.data.network.model

import com.google.firebase.firestore.DocumentId

data class Garden(
    @DocumentId
    val id: String = "",
    val code: String = "",
    val name: String = "",
    val created: String = "",
    val groupIdList: List<String> = listOf(),
)

fun Garden.toMap(): Map<String, Any?> = hashMapOf(
    "name" to name
)