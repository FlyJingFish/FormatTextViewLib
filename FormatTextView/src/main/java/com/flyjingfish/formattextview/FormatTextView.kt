package com.flyjingfish.formattextview

import android.R
import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
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
        val strings = arrayOfNulls<String>(args.size)
        val colors = IntArray(args.size)
        val bolds = BooleanArray(args.size)
        val italics = BooleanArray(args.size)
        val underlines = BooleanArray(args.size)
        val textSizes = IntArray(args.size)
        for (i in args.indices) {
            if (args[i] != null){
                if (args[i]!!.resValue != 0) {
                    strings[i] = resources.getString(args[i]!!.resValue)
                } else {
                    strings[i] = args[i]!!.strValue
                }
                colors[i] = args[i]!!.color
                bolds[i] = args[i]!!.bold
                italics[i] = args[i]!!.italic
                underlines[i] = args[i]!!.underline
                textSizes[i] = args[i]!!.textSize
            }
        }
        setFormatText(colors, bolds,italics, underlines,textSizes, formatTextValue, strings)
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


    private fun setFormatText(
        colors: IntArray,
        bolds: BooleanArray,
        italics: BooleanArray,
        underlines: BooleanArray,
        textSizes: IntArray,
        formatTextValue: String,
        args: Array<String?>
    ) {
        val strings = arrayOfNulls<String>(args.size)
        for (i in args.indices) { //%1$s
            var start = "<a href=\"$i\">"
            var end = "</a>"
            var value = args[i]
            if (italics[i]) {
                value = "<em>$value</em>"
            }
            if (bolds[i]) {
                value = "<b>$value</b>"
            }

            strings[i] = start + value + end
        }
        val formatText = String.format(formatTextValue, *strings as Array<Any?>)
        text = getClickableHtml(formatText, colors, underlines,textSizes)
        highlightColor = resources.getColor(R.color.transparent)
        autoLinkMask = Linkify.WEB_URLS
    }

    private fun getClickableHtml(
        html: String,
        colors: IntArray,
        underlines: BooleanArray,
        textSizes: IntArray
    ): CharSequence? {
        val spannedHtml = Html.fromHtml(html)
        val clickableHtmlBuilder = SpannableStringBuilder(spannedHtml)
        val spans = clickableHtmlBuilder.getSpans(
            0, spannedHtml.length,
            URLSpan::class.java
        )
        for (i in spans.indices) {
            val span = spans[i]
            setLinkClickable(clickableHtmlBuilder, span, colors[i], underlines[i] ,textSizes[i])
        }
        return clickableHtmlBuilder
    }

    private fun setLinkClickable(
        clickableHtmlBuilder: SpannableStringBuilder,
        urlSpan: URLSpan,
        color: Int,
        underline: Boolean,
        textSize: Int
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
                //????????????
                if (color != 0) {
                    ds.color = resources.getColor(color)
                } else {
                    ds.color = currentTextColor
                }
                //????????????????????????
                ds.isUnderlineText = underline
            }
        }
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags)
        if (textSize > 0){
            clickableHtmlBuilder.setSpan(AbsoluteSizeSpan(textSize,true), start, end, flags)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
//        ????????????ClickableSpan???View.OnClickListener??????????????????
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