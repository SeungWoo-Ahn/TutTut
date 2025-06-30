package io.tuttut.presentation.util

import android.content.Context
import android.content.Intent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareUtil @Inject constructor() {
    fun shareGarden(context: Context, data: ShareGardenData) {
        val content = "[텃텃 - 즐거운 텃밭 생활의 시작]\n${data.userName}님의 ${data.gardenName}에서 텃밭 생활을 함께 해요!\n\n초대 코드: ${data.gardenCode}"
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)
        }
        val chooserTitle = "shareGardenCode"
        context.startActivity(Intent.createChooser(intent, chooserTitle))
    }
}

data class ShareGardenData(
    val userName: String,
    val gardenName: String,
    val gardenCode: String,
)