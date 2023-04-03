package com.flyjingfish.formattextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import android.util.AttributeSet
import com.flyjingfish.perfecttextviewlib.PerfectTextView

open class BaseTextView : PerfectTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun getImageSpanWidthHeight(
        viewWidth: Float,
        viewHeight: Float,
        drawable: Drawable
    ): FloatArray {
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        val imageHeightWidthRatio = drawableHeight * 1f / drawableWidth
        val viewHeightWidthRatio: Float = viewHeight / viewWidth
        val wh = FloatArray(2)
        if (imageHeightWidthRatio > viewHeightWidthRatio) {
            wh[0] = viewHeight / imageHeightWidthRatio
            wh[1] = viewHeight
        } else {
            wh[0] = viewWidth
            wh[1] = viewWidth * imageHeightWidthRatio
        }
        return wh
    }

    class FormatImageSpan(drawable: Drawable, verticalAlignment: Int) :
        ImageSpan(drawable, verticalAlignment) {

        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            if (verticalAlignment == HtmlImage.ALIGN_CENTER || verticalAlignment == FormatImage.ALIGN_CENTER) {
                val b = drawable
                val fm = paint.fontMetricsInt
                val transY = (y + fm.descent + y + fm.ascent) / 2 - b.bounds.bottom / 2
                canvas.save()
                canvas.translate(x, transY.toFloat())
                b.draw(canvas)
                canvas.restore()
            } else {
                super.draw(canvas, text, start, end, x, top, y, bottom, paint)
            }
        }

    }
}