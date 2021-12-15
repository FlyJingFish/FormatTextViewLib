package com.flyjingfish.formattextview

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class FormatText {

    @ColorRes
    var color = 0
    var bold = false
    var underline = false
    var strValue: String? = null

    @StringRes
    var intValue = 0

    constructor(color: Int, bold: Boolean, underline: Boolean, strValue: String?) {
        this.color = color
        this.bold = bold
        this.underline = underline
        this.strValue = strValue
    }

    constructor(color: Int, bold: Boolean, underline: Boolean, intValue: Int) {
        this.color = color
        this.bold = bold
        this.underline = underline
        this.intValue = intValue
    }

}