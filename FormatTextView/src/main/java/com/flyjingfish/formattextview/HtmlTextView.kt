package com.flyjingfish.formattextview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import java.lang.NullPointerException

class HtmlTextView : BaseTextView {
    private var onHtmlClickListener: OnHtmlClickListener? = null
    private var onInflateImageListener: OnInflateImageListener? = null
    private var isClickSpanItem = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun setHtmlText(htmlText: String, htmlImage: HtmlImage?) {
        setHtmlText(htmlText, htmlImage, false)
    }

    fun setHtmlText(htmlText: String, htmlImage: HtmlImage?, isDeleteWrap: Boolean) {
        text = getHtml(htmlText, htmlImage, isDeleteWrap)
        highlightColor = Color.TRANSPARENT
        autoLinkMask = Linkify.WEB_URLS
    }

    fun setHtmlText(htmlText: String) {
        setHtmlText(htmlText, null)
    }

    fun setHtmlText(htmlText: String, isDeleteWrap: Boolean) {
        setHtmlText(htmlText, null, isDeleteWrap)
    }

    private fun getHtml(
        webText: String,
        htmlImage: HtmlImage?,
        isDeleteWrap: Boolean
    ): CharSequence? {
        val spanned = Html.fromHtml(webText)
        var htmlBuilder = SpannableStringBuilder(spanned)
        if (isDeleteWrap) {
            htmlBuilder = deleteLines(htmlBuilder)
        }

        val imageSpans = htmlBuilder.getSpans(
            0, spanned.length,
            ImageSpan::class.java
        )
        for (i in imageSpans.indices) {
            val imageSpan = imageSpans[i]
            setImageSpanClickable(htmlBuilder, imageSpan, htmlImage)
        }

        val urlSpans = htmlBuilder.getSpans(
            0, spanned.length,
            URLSpan::class.java
        )
        for (i in urlSpans.indices) {
            val urlSpan = urlSpans[i]
            setUrlSpanClickable(htmlBuilder, urlSpan)
        }
        return htmlBuilder
    }

    private fun setUrlSpanClickable(htmlBuilder: SpannableStringBuilder, urlSpan: URLSpan) {
        val start = htmlBuilder.getSpanStart(urlSpan)
        val end = htmlBuilder.getSpanEnd(urlSpan)
        val flags = htmlBuilder.getSpanFlags(urlSpan)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val url = urlSpan.url
                if (onHtmlClickListener != null) {
                    onHtmlClickListener!!.onUrlSpanClick(url)
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


    private fun setImageSpanClickable(
        htmlBuilder: SpannableStringBuilder,
        imageSpan: ImageSpan,
        htmlImage: HtmlImage?
    ) {
        val start = htmlBuilder.getSpanStart(imageSpan)
        val end = htmlBuilder.getSpanEnd(imageSpan)
        val flags = htmlBuilder.getSpanFlags(imageSpan)

        htmlBuilder.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                val url = imageSpan.source
                onHtmlClickListener?.onImageSpanClick(url)
                isClickSpanItem = true
            }

        }, start, end, flags)

        val viewWidth: Float = if (htmlImage != null && htmlImage.maxWidth > 0) Utils.dp2px(
            context,
            htmlImage.maxWidth
        ) else imageSpan.drawable.intrinsicWidth.toFloat()
        val viewHeight: Float = if (htmlImage != null && htmlImage.maxHeight > 0) Utils.dp2px(
            context,
            htmlImage.maxHeight
        ) else imageSpan.drawable.intrinsicHeight.toFloat()
        val drawable = LevelListDrawable()
        htmlBuilder.setSpan(
            FormatImageSpan(
                drawable,
                htmlImage?.verticalAlignment ?: HtmlImage.ALIGN_CENTER
            ), start, end, flags
        )
        if (onInflateImageListener != null) {
            onInflateImageListener?.onInflate(
                imageSpan.source,
                object : FormatTextView.OnReturnDrawableListener {
                    override fun onReturnDrawable(d: Drawable) {
                        val insetDrawable = InsetDrawable(
                            d,
                            0,
                            0,
                            0,
                            0
                        )

                        val wh = getImageSpanWidthHeight(
                            viewWidth,
                            viewHeight,
                            d
                        )
                        drawable.addLevel(2, 2, insetDrawable)
                        drawable.setBounds(
                            0, 0,
                            wh[0].toInt(),
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


    interface OnHtmlClickListener {
        fun onUrlSpanClick(url: String?)
        fun onImageSpanClick(url: String?)
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

    fun setOnInflateImageListener(onInflateImageListener: OnInflateImageListener?) {
        this.onInflateImageListener = onInflateImageListener
    }

    interface OnInflateImageListener {
        fun onInflate(source: String?, drawableListener: FormatTextView.OnReturnDrawableListener?)
    }
}