package com.flyjingfish.formattextview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import com.flyjingfish.FormatTexttextview.FormatText

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

        text = getClickableHtml(richText, *args)
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


    private fun getClickableHtml(html: String,vararg args: FormatText?): CharSequence? {
        val spannedHtml = Html.fromHtml(html)
        val clickableHtmlBuilder = SpannableStringBuilder(spannedHtml)
        val spans = clickableHtmlBuilder.getSpans(
            0, spannedHtml.length,
            URLSpan::class.java
        )
        for (i in spans.indices) {
            val span = spans[i]
            val pos = span.url.toInt()

            args[pos]?.let { setLinkClickable(clickableHtmlBuilder, span, it) }
        }
        return clickableHtmlBuilder
    }

    private fun setLinkClickable(
        clickableHtmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        formatText: FormatText
    ) {
        val color: Int = formatText.color
        val underline: Boolean = formatText.underline
        val textSize: Int = formatText.textSize
        val bold: Boolean = formatText.bold
        val italic: Boolean = formatText.italic
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
        if (textSize > 0){
            clickableHtmlBuilder.setSpan(AbsoluteSizeSpan(textSize,true), start, end, flags)
        }
        if (bold) {
            clickableHtmlBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, flags)
        }
        if (italic) {
            clickableHtmlBuilder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flags)
        }
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