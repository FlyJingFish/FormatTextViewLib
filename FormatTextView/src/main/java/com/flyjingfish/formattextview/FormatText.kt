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
    var textSize = 0f

    @JvmField
    @ColorRes
    var underlineColor = 0

    @JvmField
    var underlineWidth = 0f

    @JvmField
    var underlineMarginTop = 0f

    @JvmField
    var deleteLine = false

    @JvmField
    @ColorRes
    var deleteLineColor = 0

    @JvmField
    var deleteLineWidth = 0f


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

    fun setTextSize(textSize: Float): FormatText {
        this.textSize = textSize
        return this
    }


    fun setUnderlineColor(@ColorRes underlineColor: Int): FormatText {
        this.underlineColor = underlineColor
        return this
    }

    fun setUnderlineWidth(underlineWidth: Float): FormatText {
        this.underlineWidth = underlineWidth
        return this
    }

    fun setUnderlineMarginTop(underlineMarginTop: Float): FormatText {
        this.underlineMarginTop = underlineMarginTop
        return this
    }

    fun setBackgroundColor(@ColorRes backgroundColor: Int): FormatText {
        this.backgroundColor = backgroundColor
        return this
    }

    fun setDeleteLine(deleteLine: Boolean): FormatText {
        this.deleteLine = deleteLine
        return this
    }

    fun setDeleteLineColor(@ColorRes deleteLineColor: Int): FormatText {
        this.deleteLineColor = deleteLineColor
        return this
    }

    fun setDeleteLineWidth(deleteLineWidth: Float): FormatText {
        this.deleteLineWidth = deleteLineWidth
        return this
    }

}