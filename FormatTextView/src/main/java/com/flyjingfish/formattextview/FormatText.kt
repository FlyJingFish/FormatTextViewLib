package com.flyjingfish.FormatTexttextview

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class FormatText {

    @ColorRes
    var color = 0
    var bold = false
    var underline = false
    var italic = false
    var strValue: String? = null

    @StringRes
    var resValue = 0
    var textSize = 0;

    fun setTextColor(@ColorRes color: Int): FormatText {
        this.color = color
        return this
    }

    fun setTextBold(bold: Boolean): FormatText {
        this.bold = bold
        return this
    }

    fun setTextUnderline(underline: Boolean): FormatText {
        this.underline = underline
        return this
    }

    fun setTextItalic(italic: Boolean): FormatText {
        this.italic = italic
        return this
    }

    fun setTextStrValue(strValue: String?): FormatText {
        this.strValue = strValue
        return this
    }

    fun setTextResValue(@StringRes resValue: Int): FormatText {
        this.resValue = resValue
        return this
    }

    fun setTextSizes(textSize: Int): FormatText {
        this.textSize = textSize
        return this
    }

}