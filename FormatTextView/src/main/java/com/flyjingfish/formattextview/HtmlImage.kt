package com.flyjingfish.formattextview

import android.text.style.ImageSpan
import androidx.annotation.DrawableRes

class HtmlImage {
    companion object StaticField {
        @JvmField
        val ALIGN_BOTTOM: Int = ImageSpan.ALIGN_BOTTOM

        @JvmField
        val ALIGN_BASELINE: Int = ImageSpan.ALIGN_BASELINE

        @JvmField
        var ALIGN_CENTER: Int = 10
    }


    @JvmField
    @DrawableRes
    var imagePlaceHolder = 0

    @JvmField
    var maxWidth = 0f

    @JvmField
    var maxHeight = 0f

    @JvmField
    var verticalAlignment = ALIGN_BASELINE


    fun setMaxWidth(width: Float): HtmlImage {
        this.maxWidth = width
        return this
    }

    fun setMaxHeight(height: Float): HtmlImage {
        this.maxHeight = height
        return this
    }

    fun setVerticalAlignment(verticalAlignment: Int): HtmlImage {
        this.verticalAlignment = verticalAlignment
        return this
    }

    fun setImagePlaceHolder(@DrawableRes imagePlaceHolder: Int): HtmlImage {
        this.imagePlaceHolder = imagePlaceHolder
        return this
    }
}