package com.flyjingfish.formattextview

import android.text.style.ImageSpan
import androidx.annotation.DrawableRes

class FormatImage : BaseFormat() {
    companion object StaticField {
        @kotlin.jvm.JvmField
        val ALIGN_BOTTOM: Int = ImageSpan.ALIGN_BOTTOM

        @kotlin.jvm.JvmField
        val ALIGN_BASELINE: Int = ImageSpan.ALIGN_BASELINE

        @kotlin.jvm.JvmField
        var ALIGN_CENTER: Int = ImageSpan.ALIGN_CENTER
    }


    @JvmField
    var imageUrlValue: String? = null

    @JvmField
    @DrawableRes
    var imageResValue = 0

    @JvmField
    @DrawableRes
    var imagePlaceHolder = 0

    @JvmField
    var width = 0f

    @JvmField
    var height = 0f

    @JvmField
    var verticalAlignment = ALIGN_BASELINE

    @JvmField
    var marginLeft = 0f

    @JvmField
    var marginRight = 0f

    @JvmField
    var marginStart = 0f

    @JvmField
    var marginEnd = 0f

    fun setImageUrlValue(imageUrlValue: String?): FormatImage {
        this.imageUrlValue = imageUrlValue
        return this
    }

    fun setImageResValue(@DrawableRes imageResValue: Int): FormatImage {
        this.imageResValue = imageResValue
        return this
    }

    fun setWidth(width: Float): FormatImage {
        this.width = width
        return this
    }

    fun setHeight(height: Float): FormatImage {
        this.height = height
        return this
    }

    fun setVerticalAlignment(verticalAlignment: Int): FormatImage {
        this.verticalAlignment = verticalAlignment
        return this
    }

    fun setImagePlaceHolder(@DrawableRes imagePlaceHolder: Int): FormatImage {
        this.imagePlaceHolder = imagePlaceHolder
        return this
    }

    fun setMarginLeft(marginLeft: Float): FormatImage {
        this.marginLeft = marginLeft
        return this
    }

    fun setMarginRight(marginRight: Float): FormatImage {
        this.marginRight = marginRight
        return this
    }

    fun setMarginStart(marginStart: Float): FormatImage {
        this.marginStart = marginStart
        return this
    }

    fun setMarginEnd(marginEnd: Float): FormatImage {
        this.marginEnd = marginEnd
        return this
    }

}