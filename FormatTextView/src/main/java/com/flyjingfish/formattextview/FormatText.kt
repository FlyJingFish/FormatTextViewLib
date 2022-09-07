package com.flyjingfish.FormatTexttextview

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.flyjingfish.formattextview.BaseFormat

class FormatText : BaseFormat() {

    @JvmField
    @ColorRes
    var textColor = 0

    @JvmField
    var bold = false

    @JvmField
    var underline = false

    @JvmField
    var italic = false

    @JvmField
    var strValue: String? = null

    @JvmField
    @StringRes
    var resValue = 0

    @JvmField
    var textSize = 0

    @JvmField
    @ColorRes
    var underlineColor = 0

    @JvmField
    var underlineWidth = 0f

    @JvmField
    var underlineTopForBaseline = 0f

    fun setTextColor(@ColorRes color: Int): FormatText {
        this.textColor = color
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


    fun setUnderlineColor(@ColorRes color: Int): FormatText {
        this.underlineColor = color
        return this
    }

    fun setUnderlineWidth(width: Float): FormatText {
        this.underlineWidth = width
        return this
    }

    fun setUnderlineTopForBaseline(underlineTop: Float): FormatText {
        this.underlineTopForBaseline = underlineTop
        return this
    }


}