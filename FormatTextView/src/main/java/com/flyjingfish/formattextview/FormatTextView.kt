package com.flyjingfish.formattextview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LevelListDrawable
import android.os.Build
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.*
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.LayoutDirection
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.flyjingfish.FormatTexttextview.FormatText
import java.lang.NullPointerException
import androidx.core.text.TextUtilsCompat
import java.util.*
import kotlin.math.max


class FormatTextView : AppCompatTextView {

    private var onFormatClickListener: OnFormatClickListener? = null
    private var onInflateImageListener: OnInflateImageListener? = null
    private var isClickSpanItem = false
    var isSetOnClick = false
    private val underLineTexts: ArrayList<UnderLineText> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setFormatTextBean(@StringRes formatTextRes: Int, vararg args: BaseFormat?) {
        setFormatTextBean(resources.getString(formatTextRes), *args)
    }

    fun setFormatTextBean(formatTextValue: String, vararg args: BaseFormat?) {
        var textValue = formatTextValue.replace("\\r\\n".toRegex(), "<br>")
        textValue = textValue.replace("\\n".toRegex(), "<br>")
        textValue = textValue.replace("\\r".toRegex(), "<br>")
        val strings = arrayOfNulls<String>(args.size)
        for (i in args.indices) { //%1$s
            val start = "<a href=\"$i\">"
            val end = "</a>"

            if (args[i] is FormatImage) {
                val formatImage = args[i] as FormatImage
                val isRtl =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL
                    } else {
                        false
                    }
                formatImage.width = dp2px(formatImage.width)
                formatImage.height = dp2px(formatImage.height)
                formatImage.marginLeft = dp2px(
                    max(
                        if (isRtl) formatImage.marginEnd else formatImage.marginStart,
                        formatImage.marginLeft
                    )
                )
                formatImage.marginRight = dp2px(
                    max(
                        if (isRtl) formatImage.marginStart else formatImage.marginEnd,
                        formatImage.marginRight
                    )
                )

                strings[i] =
                    start + "<img src=\"" + (if (formatImage.imageResValue != 0) formatImage.imageResValue else formatImage.imageUrlValue) + "\"></img>" + end
            } else {
                val formatText = args[i] as FormatText
                val value: String? = if (formatText.resValue != 0) {
                    resources.getString(formatText.resValue)
                } else {
                    formatText.strValue
                }
                var value1 = value?.replace("\\r\\n".toRegex(), "<br>")
                value1 = value1?.replace("\\n".toRegex(), "<br>")
                value1 = value1?.replace("\\r".toRegex(), "<br>")
                strings[i] = start + value1 + end
            }


        }
        val richText = String.format(textValue, *strings)

        text = getCustomStyleHtml(richText, *args)
        highlightColor = Color.TRANSPARENT
        autoLinkMask = Linkify.WEB_URLS
    }


    fun setFormatText(@StringRes formatTextRes: Int, vararg args: Int) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String, vararg args: Int) {
        val formatTexts: Array<FormatText?> = arrayOfNulls<FormatText>(args.size)
        for (i in 0 until args.size) {
            formatTexts[i] = FormatText().setResValue(args[i])
        }
        setFormatTextBean(formatTextValue, *formatTexts)
    }

    fun setFormatText(@StringRes formatTextRes: Int, vararg args: String) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String, vararg args: String) {
        val formatTexts: Array<FormatText?> = arrayOfNulls<FormatText>(args.size)
        for (i in 0 until args.size) {
            formatTexts[i] = FormatText().setStrValue(args[i])
        }
        setFormatTextBean(formatTextValue, *formatTexts)
    }


    private fun getCustomStyleHtml(html: String, vararg args: BaseFormat?): CharSequence? {
        val spannedHtml = Html.fromHtml(html)
        val htmlBuilder = SpannableStringBuilder(spannedHtml)
        val spans = htmlBuilder.getSpans(
            0, spannedHtml.length,
            URLSpan::class.java
        )
        for (i in spans.indices) {
            val span = spans[i]
            val pos = span.url.toInt()

            if (args[pos] is FormatImage) {
                setImageLinkStyle(htmlBuilder, span, args[pos] as FormatImage)
            } else {
                setTextLinkStyle(htmlBuilder, span, args[pos] as FormatText)
            }
        }
        return htmlBuilder
    }

    private fun setImageLinkStyle(
        htmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        formatText: FormatImage
    ) {
        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        val clickableSpan: ClickableSpan = object : FormatClickableSpan(urlSpan) {

        }

        htmlBuilder.setSpan(clickableSpan, start, end, flags)

        val drawable = LevelListDrawable()
        if (formatText.imageResValue != 0) {
            val d = resources.getDrawable(formatText.imageResValue)
            val wh = getImageSpanWidthHeight(if (formatText.width != 0f) formatText.width else d.intrinsicWidth.toFloat(),
                if (formatText.height != 0f) formatText.height else d.intrinsicHeight.toFloat(),
                d)
            val insetDrawable =
                InsetDrawable(
                    d,
                    formatText.marginLeft.toInt(),
                    0,
                    formatText.marginRight.toInt(),
                    0
                )
            drawable.addLevel(1, 1, insetDrawable)
            drawable.setBounds(
                0, 0,
                wh[0].toInt() + formatText.marginLeft.toInt() + formatText.marginRight.toInt(),
                wh[1].toInt()
            )
            drawable.level = 1
            val imageSpan = FormatImageSpan(drawable, formatText.verticalAlignment)
            htmlBuilder.setSpan(imageSpan, start, end, flags)
        } else {
            if (formatText.imagePlaceHolder != 0) {
                val d = resources.getDrawable(formatText.imagePlaceHolder)
                val wh = getImageSpanWidthHeight(if (formatText.width != 0f) formatText.width else d.intrinsicWidth.toFloat(),
                    if (formatText.height != 0f) formatText.height else d.intrinsicHeight.toFloat(),
                    d)
                val insetDrawable =
                    InsetDrawable(
                        d,
                        formatText.marginLeft.toInt(),
                        0,
                        formatText.marginRight.toInt(),
                        0
                    )
                drawable.addLevel(1, 1, insetDrawable)
                drawable.setBounds(
                    0, 0,
                    wh[0].toInt() + formatText.marginLeft.toInt() + formatText.marginRight.toInt(),
                    wh[1].toInt()
                )
                drawable.level = 1
            }
            val imageSpan = FormatImageSpan(drawable, formatText.verticalAlignment)
            htmlBuilder.setSpan(imageSpan, start, end, flags)
            if (onInflateImageListener != null) {
                onInflateImageListener!!.onInflate(
                    formatText,
                    object : OnReturnDrawableListener {
                        override fun onReturnDrawable(d: Drawable) {
                            val insetDrawable = InsetDrawable(
                                d,
                                formatText.marginLeft.toInt(),
                                0,
                                formatText.marginRight.toInt(),
                                0
                            )
                            val wh = getImageSpanWidthHeight(if (formatText.width != 0f) formatText.width else d.intrinsicWidth.toFloat(),
                                if (formatText.height != 0f) formatText.height else d.intrinsicHeight.toFloat(),
                                d)
                            drawable.addLevel(2, 2, insetDrawable)
                            drawable.setBounds(
                                0, 0,
                                wh[0].toInt() + formatText.marginLeft.toInt() + formatText.marginRight.toInt(),
                                wh[1].toInt()
                            )
                            drawable.level = 2
                            invalidate()
                            text = text
                        }
                    })
            } else {
                throw NullPointerException("If contain url for FormatImage,must call setOnInflateImageListener before setFormatText")
            }
        }
    }

    private fun getImageSpanWidthHeight(viewWidth: Float, viewHeight: Float, drawable: Drawable): FloatArray {
        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        val imageHeightWidthRatio = drawableHeight * 1f / drawableWidth
        val viewHeightWidthRatio: Float = viewHeight / viewWidth
        val wh = FloatArray(2)
        if (imageHeightWidthRatio > viewHeightWidthRatio){
            wh[0] = viewHeight / imageHeightWidthRatio
            wh[1] = viewHeight
        }else{
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
            if (verticalAlignment == ALIGN_CENTER) {
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

    private fun setTextLinkStyle(
        htmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        formatText: FormatText
    ) {
        val color: Int = formatText.textColor
        val underline: Boolean = formatText.underline
        val textSize: Int = formatText.textSize
        val bold: Boolean = formatText.bold
        val italic: Boolean = formatText.italic
        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        var userDefaultUnder = true
        if (underline && (formatText.underlineColor != 0 || formatText.underlineTopForBaseline != 0f || formatText.underlineWidth != 0f)) {
            val underLineText = UnderLineText(
                start,
                end,
                if (formatText.underlineColor != 0) resources.getColor(formatText.underlineColor) else currentTextColor,
                dp2px(formatText.underlineTopForBaseline),
                if (formatText.underlineWidth == 0f) 1f else dp2px(formatText.underlineWidth)
            )
            underLineTexts.add(underLineText)
            userDefaultUnder = false
        }

        val clickableSpan: ClickableSpan = object : FormatClickableSpan(urlSpan) {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //设置颜色
                if (color != 0) {
                    ds.color = resources.getColor(color)
                } else {
                    ds.color = currentTextColor
                }
                //设置是否要下划线
                ds.isUnderlineText = userDefaultUnder
            }
        }
        htmlBuilder.setSpan(clickableSpan, start, end, flags)
        if (textSize > 0) {
            htmlBuilder.setSpan(AbsoluteSizeSpan(textSize, true), start, end, flags)
        }
        if (bold) {
            htmlBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, flags)
        }
        if (italic) {
            htmlBuilder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flags)
        }
    }

    open inner class FormatClickableSpan(private val urlSpan: URLSpan) : ClickableSpan() {
        override fun onClick(widget: View) {
            val url = urlSpan.url
            onFormatClickListener?.onLabelClick(url.toInt())
            isClickSpanItem = true
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
//        为了处理ClickableSpan和View.OnClickListener点击事件冲突
        super.setOnClickListener {
            if (!isClickSpanItem) {
                l?.onClick(this@FormatTextView)
            }
            isClickSpanItem = false
        }
        isSetOnClick = l != null
    }

    fun setOnFormatClickListener(onFormatClickListener: OnFormatClickListener) {
        movementMethod = ClickableMovementMethod.instance
        this.onFormatClickListener = onFormatClickListener
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawUnderline(canvas)
    }

    private fun drawUnderline(canvas: Canvas?) {
        if (underLineTexts.size == 0) {
            return
        }
        val underlinePaint = Paint()
        underlinePaint.isAntiAlias = true

        val saveCount = canvas!!.saveCount
        canvas.save()
        //绘制下划线
        for (underLineText in underLineTexts) {
            val pts = FloatArray((underLineText.end - underLineText.start) * 4)
            var ptsIndex = 0
            for (i in underLineText.start until underLineText.end) {
                val bound = getTextBound(i)
                pts[ptsIndex + 0] = bound.left.toFloat()
                pts[ptsIndex + 1] = bound.bottom.toFloat() + underLineText.underLineTop
                pts[ptsIndex + 2] = bound.right.toFloat()
                pts[ptsIndex + 3] = bound.bottom.toFloat() + underLineText.underLineTop
                ptsIndex += 4
            }
            underlinePaint.strokeWidth = underLineText.underLineWidth
            underlinePaint.color = underLineText.underLineColor
            canvas.drawLines(pts, underlinePaint)
        }
        canvas.restoreToCount(saveCount)
    }

    private fun getTextBound(index: Int): Rect {
        val layout = layout
        val bound = Rect()
        val line = layout.getLineForOffset(index)
        layout.getLineBounds(line, bound)
        bound.bottom = layout.getLineBaseline(line)
        bound.left = layout.getPrimaryHorizontal(index).toInt()
        bound.right = layout.getPrimaryHorizontal(index + 1).toInt()
        if (bound.right < bound.left) {
            bound.right = layout.getLineRight(line).toInt()
        }
        return bound;
    }

    private class UnderLineText(
        var start: Int,
        var end: Int,
        var underLineColor: Int,
        var underLineTop: Float,
        var underLineWidth: Float
    ) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (underLineTexts.size > 0) {

            val underLineText = underLineTexts[underLineTexts.size - 1]
            val line = layout.getLineForOffset(underLineText.end - 1)
            if (measuredHeight - paddingTop - paddingBottom == layout.height && line == lineCount - 1) {
                setMeasuredDimension(
                    measuredWidth,
                    measuredHeight + underLineText.underLineTop.toInt() + underLineText.underLineWidth.toInt()
                )
            }
        }


    }

    interface OnInflateImageListener {
        fun onInflate(formatImage: FormatImage?, drawableListener: OnReturnDrawableListener?)
    }

    interface OnReturnDrawableListener {
        fun onReturnDrawable(drawable: Drawable)
    }

    fun setOnInflateImageListener(onInflateImageListener: OnInflateImageListener?) {
        this.onInflateImageListener = onInflateImageListener
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}