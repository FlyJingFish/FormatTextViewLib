package com.flyjingfish.formattextview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Html
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.flyjingfish.FormatTexttextview.FormatText
import java.util.ArrayList

class FormatTextView : AppCompatTextView {

    private var onFormatClickListener: OnFormatClickListener? = null
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

    fun setFormatTextBean(@StringRes formatTextRes: Int, vararg args: FormatText?) {
        setFormatTextBean(resources.getString(formatTextRes), *args)
    }

    fun setFormatTextBean(formatTextValue: String, vararg args: FormatText?) {
        var textValue = formatTextValue.replace("\\r\\n".toRegex(), "<br>")
        textValue = textValue.replace("\\n".toRegex(), "<br>")
        textValue = textValue.replace("\\r".toRegex(), "<br>")
        val strings = arrayOfNulls<String>(args.size)
        for (i in args.indices) { //%1$s
            val start = "<a href=\"$i\">"
            val end = "</a>"
            var value: String? = if (args[i]!!.resValue != 0) {
                resources.getString(args[i]!!.resValue)
            } else {
                args[i]!!.strValue
            }
            strings[i] = start + value + end
        }
        val richText = String.format(textValue, *strings as Array<Any?>)

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
            formatTexts[i] = FormatText().setTextResValue(args[i])
        }
        setFormatTextBean(formatTextValue, *formatTexts)
    }

    fun setFormatText(@StringRes formatTextRes: Int, vararg args: String) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String, vararg args: String) {
        val formatTexts: Array<FormatText?> = arrayOfNulls<FormatText>(args.size)
        for (i in 0 until args.size) {
            formatTexts[i] = FormatText().setTextStrValue(args[i])
        }
        setFormatTextBean(formatTextValue, *formatTexts)
    }


    private fun getCustomStyleHtml(html: String, vararg args: FormatText?): CharSequence? {
        val spannedHtml = Html.fromHtml(html)
        val htmlBuilder = SpannableStringBuilder(spannedHtml)
        val spans = htmlBuilder.getSpans(
            0, spannedHtml.length,
            URLSpan::class.java
        )
        for (i in spans.indices) {
            val span = spans[i]
            val pos = span.url.toInt()

            args[pos]?.let { setLinkStyle(htmlBuilder, span, it) }
        }
        return htmlBuilder
    }
    private fun setLinkStyle(
        htmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        formatText: FormatText
    ) {
        val color: Int = formatText.color
        val underline: Boolean = formatText.underline
        val textSize: Int = formatText.textSize
        val bold: Boolean = formatText.bold
        val italic: Boolean = formatText.italic
        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        if (underline) {
            // TODO: 2022/9/6 写一下距离字体baseline的下划线距离
            val underLineText = UnderLineText(
                start, end,
                if (color != 0) resources.getColor(color) else currentTextColor
            ,90,12f)
            underLineTexts.add(underLineText)
        }

        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val url = urlSpan.url
                onFormatClickListener?.onLabelClick(url.toInt())
                isClickSpanItem = true
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                //设置颜色
                if (color != 0) {
                    ds.color = resources.getColor(color)
                } else {
                    ds.color = currentTextColor
                }
                //设置是否要下划线
                ds.isUnderlineText = false
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
        drawBgShape(canvas)
    }

    private fun drawBgShape(canvas: Canvas?) {
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
        bound.right = layout.getPrimaryHorizontal(index+1).toInt()
        if (bound.right < bound.left){
            bound.right = layout.getLineRight(line).toInt()
        }
        return bound;
    }

    private class UnderLineText(var start: Int, var end: Int, var underLineColor: Int,var underLineTop: Int,var underLineWidth: Float) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (underLineTexts.size > 0){

            val underLineText = underLineTexts[underLineTexts.size-1]
            val line = layout.getLineForOffset(underLineText.end - 1)
            val layout = layout
            val bound = Rect()
            layout.getLineBounds(lineCount-1, bound)
            if (measuredHeight - paddingTop - paddingBottom == layout.height && line == lineCount-1) {
                setMeasuredDimension(measuredWidth,measuredHeight+ underLineText.underLineTop + underLineText.underLineWidth.toInt())
            }
            Log.e("onMeasure",this.toString()+measuredHeight.toString() + "=="+bound.bottom+"==="+layout.height)
//            setMeasuredDimension(measuredWidth,measuredHeight+lineSpacingExtra.toInt())
        }


    }
}