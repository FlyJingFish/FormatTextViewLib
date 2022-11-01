package com.flyjingfish.formattextview

import android.content.Context
import android.graphics.Color
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

class HtmlTextView : AppCompatTextView {
    private var onHtmlClickListener: OnHtmlClickListener? = null
    private var isClickSpanItem = false
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setHtmlText(htmlText: String) {
        setHtmlText(htmlText, false)
    }

    fun setHtmlText(htmlText: String, isDeleteWrap: Boolean) {
        text = setClickableHtml(htmlText, isDeleteWrap)
        highlightColor = Color.TRANSPARENT
        autoLinkMask = Linkify.WEB_URLS
    }

    private fun setClickableHtml(webText: String, isDeleteWrap: Boolean): CharSequence? {
        val spanned = Html.fromHtml(webText)
        var htmlBuilder = SpannableStringBuilder(spanned)
        if (isDeleteWrap) {
            htmlBuilder = deleteLines(htmlBuilder)
        }
        val spans = htmlBuilder.getSpans(
            0, spanned.length,
            URLSpan::class.java
        )
        for (i in spans.indices) {
            val span = spans[i]
            setSpanClickable(htmlBuilder, span)
        }
        return htmlBuilder
    }

    private fun setSpanClickable(htmlBuilder: SpannableStringBuilder, urlSpan: URLSpan) {
        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val url = urlSpan.url
                if (onHtmlClickListener != null) {
                    onHtmlClickListener!!.onLabelClick(url)
                    isClickSpanItem = true
                }
            }
        }
        htmlBuilder.setSpan(clickableSpan, start, end, flags)
    }

    fun deleteLines(spannableStringBuilder: SpannableStringBuilder): SpannableStringBuilder {
        val length = spannableStringBuilder.length
        for (i in 0 until length) {
            val ch = spannableStringBuilder[i]
            if (ch == '\r' || ch == '\n') {
                return deleteLines(spannableStringBuilder.delete(i, i + 1))
            }
        }
        return spannableStringBuilder
    }


    interface OnHtmlClickListener {
        fun onLabelClick(url: String?)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener { v: View? ->
            if (!isClickSpanItem && l != null) {
                l.onClick(this@HtmlTextView)
            }
            isClickSpanItem = false
        }
    }

    fun setOnHtmlClickListener(onHtmlClickListener: OnHtmlClickListener?) {
        movementMethod = LinkMovementMethod.getInstance()
        this.onHtmlClickListener = onHtmlClickListener
    }
}