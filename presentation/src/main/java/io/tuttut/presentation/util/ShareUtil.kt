package io.tuttut.presentation.util

import android.content.Context
import android.content.Intent
import io.tuttut.data.model.dto.Garden
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareUtil @Inject constructor() {
    fun shareGarden(context: Context, garden: Garden, userName: String) {
        val content = "[텃텃 - 즐거운 텃밭 생활의 시작]\n${userName}님의 ${garden.name}에서 텃밭 생활을 함께 해요!\n\n초대 코드: ${garden.code}"
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)
        }
        val chooserTitle = "shareGardenCode"
        context.startActivity(Intent.createChooser(intent, chooserTitle))
    }
}