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

    fun setColor(@ColorRes color: Int): FormatText {
        this.color = color
        return this
    }

    fun setBold(bold: Boolean): FormatText {
        this.bold = bold
        return this
    }

    fun setUnderline(underline: Boolean): FormatText {
        this.underline = underline
        return this
    }

    fun setItalic(italic: Boolean): FormatText {
        this.italic = italic
        return this
    }

    fun setStrValue(strValue: String?): FormatText {
        this.strValue = strValue
        return this
    }

    fun setResValue(@StringRes resValue: Int): FormatText {
        this.resValue = resValue
        return this
    }

    fun setTextSize(textSize: Int): FormatText {
        this.textSize = textSize
        return this
    }

}