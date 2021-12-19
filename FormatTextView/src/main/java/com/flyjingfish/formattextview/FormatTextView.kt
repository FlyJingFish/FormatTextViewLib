package com.flyjingfish.formattextview

import android.R
import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView

class FormatTextView :AppCompatTextView {

    private var onFormatClickListener: OnFormatClickListener? = null
    private var isClickSpanItem = false
    var isSetOnClick = false

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
        val strings = arrayOfNulls<String>(args.size)
        val colors = IntArray(args.size)
        val bolds = BooleanArray(args.size)
        val underlines = BooleanArray(args.size)
        for (i in 0 until args.size) {
            if (args[i] != null){
                if (args[i]!!.intValue != 0) {
                    strings[i] = resources.getString(args[i]!!.intValue)
                } else {
                    strings[i] = args[i]!!.strValue
                }
                colors[i] = args[i]!!.color
                bolds[i] = args[i]!!.bold
                underlines[i] = args[i]!!.underline
            }
        }
        setFormatText(colors, bolds, underlines, formatTextValue, strings)
    }


    fun setFormatText(@StringRes formatTextRes: Int, vararg args: Int) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String, vararg args: Int) {
        val formatTexts: Array<FormatText?> = arrayOfNulls<FormatText>(args.size)
        for (i in 0 until args.size) {
            formatTexts[i] = FormatText(0, false, false, args[i])
        }
        setFormatTextBean(formatTextValue, *formatTexts)
    }

    fun setFormatText(@StringRes formatTextRes: Int, vararg args: String) {
        setFormatText(resources.getString(formatTextRes), *args)
    }

    fun setFormatText(formatTextValue: String, vararg args: String) {
        val formatTexts: Array<FormatText?> = arrayOfNulls<FormatText>(args.size)
        for (i in 0 until args.size) {
            formatTexts[i] = FormatText(0, false, false, args[i])
        }
        setFormatTextBean(formatTextValue, *formatTexts)
    }


    private fun setFormatText(
        colors: IntArray,
        bolds: BooleanArray,
        underlines: BooleanArray,
        @StringRes formatTextRes: Int,
        args: Array<String?>
    ) {
        setFormatText(colors, bolds, underlines, resources.getString(formatTextRes), args)
    }

    private fun setFormatText(
        colors: IntArray,
        bolds: BooleanArray,
        underlines: BooleanArray,
        formatTextValue: String,
        args: Array<String?>
    ) {
        val strings = arrayOfNulls<String>(args.size)
        for (i in args.indices) { //%1$s
            var start = "<a href=\"$i\">"
            var end = "</a>"
            val value = "%" + (i + 1) + "\$s"
            if (bolds[i]) {
                strings[i] = "<b>$value</b>"
            } else {
                strings[i] = value
            }
            strings[i] = start + strings[i] + end
        }
        val formatText = String.format(formatTextValue, *strings as Array<Any?>)
        val showText = String.format(formatText, *args as Array<Any?>)
        text = getClickableHtml(showText, colors, underlines)
        highlightColor = resources.getColor(R.color.transparent)
        autoLinkMask = Linkify.WEB_URLS
    }

    private fun getClickableHtml(
        html: String,
        colors: IntArray,
        underlines: BooleanArray
    ): CharSequence? {
        val spannedHtml = Html.fromHtml(html)
        val clickableHtmlBuilder = SpannableStringBuilder(spannedHtml)
        val spans = clickableHtmlBuilder.getSpans(
            0, spannedHtml.length,
            URLSpan::class.java
        )
        for (i in spans.indices) {
            val span = spans[i]
            setLinkClickable(clickableHtmlBuilder, span, colors[i], underlines[i])
        }
        return clickableHtmlBuilder
    }

    private fun setLinkClickable(
        clickableHtmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        color: Int,
        underline: Boolean
    ) {
        val start = clickableHtmlBuilder.getSpanStart(urlSpan)
        val end = clickableHtmlBuilder.getSpanEnd(urlSpan)
        val flags = clickableHtmlBuilder.getSpanFlags(urlSpan)
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
                ds.isUnderlineText = underline
            }
        }
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags)
    }

    override fun setOnClickListener(l: OnClickListener?) {
//        为了处理ClickableSpan和View.OnClickListener点击事件冲突
        super.setOnClickListener {
            if (!isClickSpanItem){
                l?.onClick(this@FormatTextView)
            }
            isClickSpanItem = false
        }
        isSetOnClick = l!=null
    }

    fun setOnFormatClickListener(onFormatClickListener: OnFormatClickListener){
        movementMethod = ClickableMovementMethod.instance
        this.onFormatClickListener = onFormatClickListener
    }

}