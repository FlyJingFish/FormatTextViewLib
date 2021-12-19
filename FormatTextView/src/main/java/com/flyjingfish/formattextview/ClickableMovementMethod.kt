package com.flyjingfish.formattextview

import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.text.Spannable
import android.view.MotionEvent
import android.text.style.ClickableSpan
import android.os.Build
import android.view.ViewGroup

class ClickableMovementMethod : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val off = layout.getOffsetForHorizontal(line, x.toFloat())
            val link = buffer.getSpans(off, off, ClickableSpan::class.java)
            if (link.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget)
                }
                return true
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    if (widget.hasOnClickListeners()) {
                        return true
                    }
                } else {
                    if (widget is FormatTextView && widget.isSetOnClick) {
                        return true
                    }
                }
                if (action == MotionEvent.ACTION_UP) {
                    val parent = widget.parent
                    if (parent is ViewGroup) {
                        parent.performClick()
                    }
                }
            }
        }
        return false
    }

    companion object {
        private var sInstance: ClickableMovementMethod? = null
        val instance: ClickableMovementMethod?
            get() {
                if (sInstance == null) {
                    sInstance = ClickableMovementMethod()
                }
                return sInstance
            }
    }
}