package com.flyjingfish.formattextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
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
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import java.util.Locale


class FormatTextView : BaseTextView {

    private var onFormatClickListener: OnFormatClickListener? = null
    private var onInflateImageListener: OnInflateImageListener? = null
    private var isClickSpanItem = false
    var isSetOnClick = false
    private var isDrawGradient = false
    private var isClearTexts = true
    private val underLineTexts: ArrayList<LineText> = ArrayList()
    private val deleteLineTexts: ArrayList<LineText> = ArrayList()
    private val gradientTexts: ArrayList<LineText> = ArrayList()
    private val gradientDrawTexts: ArrayList<GradientText> = ArrayList()
    private var formatArgs: Array<out BaseFormat?>? = null
    private var richText: String ?= null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setFormatText(@StringRes formatTextRes: Int, vararg args: Int) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String?, vararg args: Int) {
        val formatTexts: Array<FormatText?> = arrayOfNulls(args.size)
        for (i in args.indices) {
            formatTexts[i] = FormatText().setResValue(args[i])
        }
        setFormatText(formatTextValue, *formatTexts)
    }

    fun setFormatText(@StringRes formatTextRes: Int, vararg args: String) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String?, vararg args: String) {
        val formatTexts: Array<FormatText?> = arrayOfNulls(args.size)
        for (i in args.indices) {
            formatTexts[i] = FormatText().setStrValue(args[i])
        }
        setFormatText(formatTextValue, *formatTexts)
    }

    fun setFormatText(@StringRes formatTextRes: Int, vararg args: BaseFormat?) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String?, vararg args: BaseFormat?) {
        var textValue = formatTextValue?.replace("\\r\\n".toRegex(), "<br>")
        textValue = textValue?.replace("\\n".toRegex(), "<br>")
        textValue = textValue?.replace("\\r".toRegex(), "<br>")
        val strings = arrayOfNulls<String>(args.size)
        var isContainGradient = false
        for (i in args.indices) { //%1$s
            val start = "<a href=\"$i\">"
            val end = "</a>"

            if (args[i] is FormatImage) {
                val formatImage = args[i] as FormatImage
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
                if (formatText.gradient != null){
                    isContainGradient = true
                }
            }


        }
        if (textValue == null){
            isClearTexts = true
            text = ""
            return
        }
        isDrawGradient = !isContainGradient
        underLineTexts.clear()
        deleteLineTexts.clear()
        gradientTexts.clear()
        gradientDrawTexts.clear()
        val richText = String.format(textValue, *strings)
        formatArgs = args
        this.richText = richText
        isClearTexts = false
        text = getCustomStyleHtml(richText, *args)
        highlightColor = Color.TRANSPARENT
        autoLinkMask = Linkify.WEB_URLS
    }

    @Deprecated("请直接使用setFormatText", ReplaceWith("setFormatText(formatTextRes, *args)"), DeprecationLevel.ERROR)
    fun setFormatTextBean(@StringRes formatTextRes: Int, vararg args: BaseFormat?) {
        setFormatText(formatTextRes, *args)
    }
    @Deprecated("请直接使用setFormatText", ReplaceWith("setFormatText(formatTextValue, *args)"), DeprecationLevel.ERROR)
    fun setFormatTextBean(formatTextValue: String?, vararg args: BaseFormat?) {
        setFormatText(formatTextValue, *args)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if (isClearTexts){
            isDrawGradient = true
            underLineTexts.clear()
            deleteLineTexts.clear()
            gradientTexts.clear()
            gradientDrawTexts.clear()
            formatArgs = null
            this.richText = null
        }
        super.setText(text, type)
    }

    private fun resetText(){
        isClearTexts = false
        text = richText?.let { formatArgs?.let { it1 -> getCustomStyleHtml(it, *it1) } }
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
        formatImage: FormatImage
    ) {
        val isRtl =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == LayoutDirection.RTL
            } else {
                false
            }
        val imageWidth = formatImage.width.dp
        val imageHeight = formatImage.height.dp
        var marginLeft = if (isRtl) formatImage.marginEnd else formatImage.marginStart
        var marginRight = if (isRtl) formatImage.marginStart else formatImage.marginEnd
        marginLeft = if (marginLeft == 0f){
            formatImage.marginLeft.dp
        }else{
            marginLeft.dp
        }

        marginRight = if (marginRight == 0f){
            formatImage.marginRight.dp
        }else{
            marginRight.dp
        }

        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        val clickableSpan: ClickableSpan = FormatClickableSpan(urlSpan)
        htmlBuilder.removeSpan(formatImage)
        htmlBuilder.setSpan(clickableSpan, start, end, flags)

        val drawable = LevelListDrawable()
        if (formatImage.imageResValue != 0) {
            val d = ContextCompat.getDrawable(context,formatImage.imageResValue)
            val wh = d?.let {
                getImageSpanWidthHeight(
                    if (imageWidth != 0f) imageWidth else d.intrinsicWidth.toFloat(),
                    if (imageHeight != 0f) imageHeight else d.intrinsicHeight.toFloat(),
                    d
                )
            }
            val insetDrawable =
                InsetDrawable(
                    d,
                    marginLeft.toInt(),
                    0,
                    marginRight.toInt(),
                    0
                )
            drawable.addLevel(1, 1, insetDrawable)
            drawable.setBounds(
                0, 0,
                (wh?.get(0)?.toInt() ?: 0) + marginLeft.toInt() + marginRight.toInt(),
                (wh?.get(1)?.toInt() ?: 0).toInt()
            )
            drawable.level = 1
            val imageSpan = FormatImageSpan(drawable, formatImage.verticalAlignment)
            htmlBuilder.setSpan(imageSpan, start, end, flags)
        } else {
            if (formatImage.imagePlaceHolder != 0) {
                val d = ContextCompat.getDrawable(context,formatImage.imagePlaceHolder)
                val wh = d?.let {
                    getImageSpanWidthHeight(
                        if (imageWidth != 0f) imageWidth else d.intrinsicWidth.toFloat(),
                        if (imageHeight != 0f) imageHeight else d.intrinsicHeight.toFloat(),
                        d
                    )
                }
                val insetDrawable =
                    InsetDrawable(
                        d,
                        marginLeft.toInt(),
                        0,
                        marginRight.toInt(),
                        0
                    )
                drawable.addLevel(1, 1, insetDrawable)
                drawable.setBounds(
                    0, 0,
                    (wh?.get(0)?.toInt() ?: 0) + marginLeft.toInt() + marginRight.toInt(),
                    (wh?.get(1)?.toInt() ?: 0).toInt()
                )
                drawable.level = 1
            }
            val imageSpan = FormatImageSpan(drawable, formatImage.verticalAlignment)
            htmlBuilder.setSpan(imageSpan, start, end, flags)
            if (onInflateImageListener != null) {
                onInflateImageListener!!.onInflate(
                    formatImage,
                    object : OnReturnDrawableListener {
                        override fun onReturnDrawable(d: Drawable) {
                            val insetDrawable = InsetDrawable(
                                d,
                                marginLeft.toInt(),
                                0,
                                marginRight.toInt(),
                                0
                            )
                            val wh = getImageSpanWidthHeight(
                                if (imageWidth != 0f) imageWidth else d.intrinsicWidth.toFloat(),
                                if (imageHeight != 0f) imageHeight else d.intrinsicHeight.toFloat(),
                                d
                            )
                            drawable.addLevel(2, 2, insetDrawable)
                            drawable.setBounds(
                                0, 0,
                                wh[0].toInt() + marginLeft.toInt() + marginRight.toInt(),
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
        setCommonLinkStyle(htmlBuilder, urlSpan, formatImage)
    }

    private fun setTextLinkStyle(
        htmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        formatText: FormatText
    ) {
        val color: Int = formatText.textColor
        val underline: Boolean = formatText.underline
        val deleteLine: Boolean = formatText.deleteLine
        val textSize: Float = formatText.textSize
        val bold: Boolean = formatText.bold
        val italic: Boolean = formatText.italic
        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        var userDefaultUnder = true
        val textColor = if (color != 0) {
            resources.getColor(color)
        } else {
            currentTextColor
        }
        if (underline && (formatText.underlineColor != 0 || formatText.underlineMarginTop != 0f || formatText.underlineWidth != 0f)) {
            val textPaint = TextPaint()
            textPaint.textSize = if (textSize > 0) textSize.sp else getTextSize()
            val fm = textPaint.fontMetrics

            val underLineText = LineText(
                start,
                end,
                if (formatText.underlineColor != 0) resources.getColor(formatText.underlineColor) else textColor,
                formatText.underlineMarginTop.dp + fm.descent / 3,
                if (formatText.underlineWidth == 0f) 1f.dp else formatText.underlineWidth.dp
            )
            underLineTexts.add(underLineText)
            userDefaultUnder = false
        }
        var userDefaultDelete = true
        if (deleteLine && (formatText.deleteLineColor != 0 || formatText.deleteLineWidth != 0f)) {
            val textPaint = TextPaint()
            textPaint.textSize = if (textSize > 0) textSize.sp else getTextSize()
            val fm = textPaint.fontMetrics

            val deleteLineText = LineText(
                start,
                end,
                if (formatText.deleteLineColor != 0) resources.getColor(formatText.deleteLineColor) else textColor,
                (fm.descent - fm.ascent) / 2 - fm.descent,
                if (formatText.deleteLineWidth == 0f) 1f.dp else formatText.deleteLineWidth.dp
            )
            deleteLineTexts.add(deleteLineText)
            userDefaultDelete = false

        }
        //gradient
        if (!isDrawGradient && formatText.gradient != null) {
            val textPaint = TextPaint()
            textPaint.textSize = if (textSize > 0) textSize.sp else getTextSize()
            val fm = textPaint.fontMetrics

            val gradientText = LineText(
                start,
                end,
                0,
                (fm.descent - fm.ascent) / 2 - fm.descent,
                0f
            )
            gradientTexts.add(gradientText)

        }

        if (isDrawGradient && gradientDrawTexts.size > 0){
            for (gradientDrawText in gradientDrawTexts) {
                if (gradientDrawText.start>=start && gradientDrawText.end <= end){

                    val clickableSpan: GradientSpan = object : GradientSpan() {
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            var left=0f
                            var top=0f
                            var right=0f
                            var bottom=0f
                            var orientation = formatText.gradient?.orientation
                            if (orientation == null){
                                orientation = Gradient.Orientation.LEFT_TO_RIGHT
                            }
                            when (orientation) {
                                Gradient.Orientation.LEFT_TO_RIGHT -> {
                                    left = gradientDrawText.rectF.left
                                    top = gradientDrawText.rectF.top
                                    right = gradientDrawText.rectF.right
                                    bottom = gradientDrawText.rectF.top
                                }
                                Gradient.Orientation.TOP_TO_BOTTOM -> {
                                    left = gradientDrawText.rectF.left
                                    top = gradientDrawText.rectF.top
                                    right = gradientDrawText.rectF.left
                                    bottom = gradientDrawText.rectF.bottom
                                }
                                Gradient.Orientation.LEFT_TOP_TO_RIGHT_BOTTOM -> {
                                    left = gradientDrawText.rectF.left
                                    top = gradientDrawText.rectF.top
                                    right = gradientDrawText.rectF.right
                                    bottom = gradientDrawText.rectF.bottom
                                }
                                Gradient.Orientation.LEFT_BOTTOM_TO_RIGHT_TOP -> {
                                    left = gradientDrawText.rectF.left
                                    top = gradientDrawText.rectF.bottom
                                    right = gradientDrawText.rectF.right
                                    bottom = gradientDrawText.rectF.top
                                }
                            }
                            val linearGradient = formatText.gradient?.gradientColors?.let {
                                LinearGradient(
                                    left, top, right, bottom,
                                    it, formatText.gradient?.gradientPositions,
                                    Shader.TileMode.CLAMP
                                )
                            }
                            ds.shader = linearGradient
                            //设置是否要下划线
                            ds.isUnderlineText = userDefaultUnder && underline
                            if (userDefaultDelete && deleteLine) {
                                ds.flags = ds.flags or Paint.STRIKE_THRU_TEXT_FLAG
                            }
                        }
                    }
                    htmlBuilder.setSpan(clickableSpan, gradientDrawText.start, gradientDrawText.end, flags)
                }
            }
        }

        val clickableSpan: ClickableSpan = object : FormatClickableSpan(urlSpan) {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                if(formatText.ignorePaintShader && formatText.gradient == null){
                    ds.shader = null
                }
                //设置颜色
                ds.color = textColor
                //设置是否要下划线
                ds.isUnderlineText = userDefaultUnder && underline
                if (userDefaultDelete && deleteLine) {
                    ds.flags = ds.flags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
        htmlBuilder.removeSpan(urlSpan)
        htmlBuilder.setSpan(clickableSpan, start, end, flags)
        if (textSize > 0) {
            htmlBuilder.setSpan(
                AbsoluteSizeSpan(
                    textSize.sp.toInt(),
                    false
                ), start, end, flags
            )
        }
        if (bold && italic) {
            htmlBuilder.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, flags)
        } else if (bold) {
            htmlBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, flags)
        } else if (italic) {
            htmlBuilder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flags)
        }
        setCommonLinkStyle(htmlBuilder, urlSpan, formatText)
    }

    private fun setCommonLinkStyle(
        htmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        formatText: BaseFormat
    ) {
        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        val backgroundColor: Int = formatText.backgroundColor
        if (backgroundColor != 0) {
            htmlBuilder.setSpan(
                BackgroundColorSpan(resources.getColor(backgroundColor)),
                start,
                end,
                flags
            )
        }
    }

    open inner class FormatClickableSpan(urlSpan: URLSpan) : ClickableSpan() {
        private var position = urlSpan.url.toInt()

        override fun onClick(widget: View) {
            onFormatClickListener?.onLabelClick(position)
            isClickSpanItem = true
        }
    }

    abstract class GradientSpan : CharacterStyle(), UpdateAppearance {
        override fun updateDrawState(ds: TextPaint) {
            ds.color = ds.linkColor
            ds.isUnderlineText = false
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
        drawDeleteLine(canvas)
        getGradient()
        isClearTexts = true
    }
    private fun getGradient() {
        if (gradientTexts.size == 0 || isDrawGradient) {
            return
        }
        //绘制下划线
        for (lineText in gradientTexts) {
            val map = HashMap<Int,GradientText>()
            for (i in lineText.start until lineText.end) {
                val line = layout.getLineForOffset(i)
                val bound = getUnderLineBound(i)
                val left = bound.left.toFloat()
                val top = bound.bottom.toFloat() - lineText.lineTop*2
                val right = bound.right.toFloat()
                val bottom = bound.bottom.toFloat()
                val rect = RectF(left,top,right, bottom)
                if (!map.containsKey(line)){
                    val gradientText = GradientText(rect,i,i+1)
                    map[line] = gradientText
                }else{
                    val gradientText = map[line]
                    val oldRect = gradientText!!.rectF
                    if (oldRect.left < rect.left){
                        oldRect.right = right
                    }else{
                        oldRect.left = left
                    }
                    gradientText.end = i+1
                }
            }

            for ((_,value) in map){
                gradientDrawTexts.add(value)
            }

        }
        isDrawGradient = true
        resetText()
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
                val bound = getUnderLineBound(i)
                pts[ptsIndex + 0] = bound.left.toFloat()
                pts[ptsIndex + 1] =
                    bound.bottom.toFloat() + underLineText.lineWidth / 2 + underLineText.lineTop
                pts[ptsIndex + 2] = bound.right.toFloat()
                pts[ptsIndex + 3] = pts[ptsIndex + 1]
                ptsIndex += 4
            }
            underlinePaint.strokeWidth = underLineText.lineWidth
            underlinePaint.color = underLineText.lineColor
            canvas.drawLines(pts, underlinePaint)
        }
        canvas.restoreToCount(saveCount)
    }

    private fun drawDeleteLine(canvas: Canvas?) {
        if (deleteLineTexts.size == 0) {
            return
        }
        val underlinePaint = Paint()
        underlinePaint.isAntiAlias = true

        val saveCount = canvas!!.saveCount
        canvas.save()
        //绘制下划线
        for (deleteLineText in deleteLineTexts) {
            val pts = FloatArray((deleteLineText.end - deleteLineText.start) * 4)
            var ptsIndex = 0
            for (i in deleteLineText.start until deleteLineText.end) {
                val bound = getDeleteLineBound(i)
                pts[ptsIndex + 0] = bound.left.toFloat()
                pts[ptsIndex + 1] = bound.bottom.toFloat() - deleteLineText.lineTop
                pts[ptsIndex + 2] = bound.right.toFloat()
                pts[ptsIndex + 3] = pts[ptsIndex + 1]
                ptsIndex += 4
            }
            underlinePaint.strokeWidth = deleteLineText.lineWidth
            underlinePaint.color = deleteLineText.lineColor
            canvas.drawLines(pts, underlinePaint)
        }
        canvas.restoreToCount(saveCount)
    }

    private fun getDeleteLineBound(index: Int): Rect {
        val layout = layout
        val bound = Rect()
        val line = layout.getLineForOffset(index)
        val baseline = layout.getLineBaseline(line)
        bound.bottom = baseline+compoundPaddingTop
        bound.left = layout.getPrimaryHorizontal(index).toInt()+compoundPaddingLeft
        bound.right = layout.getPrimaryHorizontal(index + 1).toInt()+compoundPaddingLeft
        if (bound.right < bound.left) {
            bound.right = layout.getLineRight(line).toInt()+compoundPaddingLeft
        }
        return bound;
    }

    private fun getUnderLineBound(index: Int): Rect {
        val layout = layout
        val bound = Rect()
        val line = layout.getLineForOffset(index)
        bound.bottom = layout.getLineBaseline(line)+compoundPaddingTop
        bound.left = layout.getPrimaryHorizontal(index).toInt()+compoundPaddingLeft
        bound.right = layout.getPrimaryHorizontal(index + 1).toInt()+compoundPaddingLeft
        if (bound.right < bound.left) {
            bound.right = layout.getLineRight(line).toInt()+compoundPaddingLeft
        }
        return bound;
    }

    private class LineText(
        var start: Int,
        var end: Int,
        var lineColor: Int,
        var lineTop: Float,
        var lineWidth: Float
    )

    private class GradientText(
        var rectF: RectF,
        var start: Int,
        var end: Int,
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (underLineTexts.size > 0) {

            val underLineText = underLineTexts[underLineTexts.size - 1]
            val line = layout.getLineForOffset(underLineText.end - 1)
            if (line == lineCount - 1 && measuredHeight - compoundPaddingTop - compoundPaddingBottom <= layout.height) {
                setMeasuredDimension(
                    measuredWidth,
                    measuredHeight + underLineText.lineTop.toInt() + underLineText.lineWidth.toInt()
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

}