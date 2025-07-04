package io.tuttut.presentation.navigation

import android.net.Uri
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import io.tuttut.domain.model.cropsInfo.CropsKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Parcelize
sealed interface AddCropsPurpose : Parcelable {
    @Parcelize
    data class ForAdd(val cropsKey: CropsKey) : AddCropsPurpose

    @Parcelize
    data class ForEdit(val cropsId: String) : AddCropsPurpose
}

val AddCropsParamsType = object :  NavType<AddCropsPurpose>(isNullableAllowed = false) {
    override fun put(bundle: SavedState, key: String, value: AddCropsPurpose) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: SavedState, key: String): AddCropsPurpose? {
        return bundle.getBundle(key) as AddCropsPurpose?
    }

    override fun serializeAsValue(value: AddCropsPurpose): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun parseValue(value: String): AddCropsPurpose {
        return Json.decodeFromString<AddCropsPurpose>(value)
    }
}

@Parcelize
sealed interface AddDiaryPurpose : Parcelable {
    @Parcelize
    data class ForAdd(val cropsId: String) : AddDiaryPurpose

    @Parcelize
    data class ForEdit(val diaryId: String) : AddDiaryPurpose
}

val AddDiaryParamsType = object :  NavType<AddDiaryPurpose>(isNullableAllowed = false) {
    override fun put(bundle: SavedState, key: String, value: AddDiaryPurpose) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: SavedState, key: String): AddDiaryPurpose? {
        return bundle.getBundle(key) as AddDiaryPurpose?
    }

    override fun serializeAsValue(value: AddDiaryPurpose): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun parseValue(value: String): AddDiaryPurpose {
        return Json.decodeFromString<AddDiaryPurpose>(value)
    }
}