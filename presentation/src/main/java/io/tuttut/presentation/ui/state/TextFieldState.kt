package io.tuttut.presentation.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class SupportingTextType {
    INFO, ERROR
}

data class SupportingText(
    val text: String,
    val type: SupportingTextType = SupportingTextType.INFO
)

interface ITextFieldState {
    var typedText: String

    var supportingText: SupportingText?

    fun typeText(text: String)

    fun resetText()

    fun getTrimmedText(): String

    fun isValidate(): Boolean
}

open class TextFieldState(
    private val maxLength: Int,
): ITextFieldState {
    override var typedText by mutableStateOf("")

    override var supportingText by mutableStateOf<SupportingText?>(SupportingText(text = "최대 ${maxLength}자"))

    override fun typeText(text: String) {
        if (text.length <= maxLength) {
            typedText = text
        }
    }

    override fun resetText() {
        typedText = ""
    }

    override fun getTrimmedText(): String = typedText.trim()

    override fun isValidate(): Boolean = getTrimmedText().isNotEmpty()
}

class CodeTextFieldState(
    private val maxLength: Int = 6
) : TextFieldState(maxLength) {
    override var supportingText by mutableStateOf<SupportingText?>(null)

    override fun typeText(text: String) {
        super.typeText(text)
        supportingText?.let { supportingText = null }
    }

    fun ifNotFound() {
        supportingText = SupportingText(
            text = "텃밭 코드를 다시 확인해주세요",
            type = SupportingTextType.ERROR
        )
    }

    override fun isValidate(): Boolean = getTrimmedText().length == maxLength
}

