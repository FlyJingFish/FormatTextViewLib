package com.flyjingfish.formattextview

import android.text.method.LinkMovementMethod
import android.widget.TextView
import android.text.Spannable
import android.view.MotionEvent
import android.text.style.ClickableSpan
import android.os.Build
import android.view.ViewGroup

class ClickableMovementMethod : LinkMovementMethod() {
    private var downTime: Long = 0
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val action = event.actionMasked
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            if (action == MotionEvent.ACTION_DOWN) {
                downTime = System.currentTimeMillis()
            }
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
            if (link.size > 0) {
                if (action == MotionEvent.ACTION_UP) {
                    val upTime = System.currentTimeMillis()
                    //按下的时间和抬起的时间差小于500ms就响应对应点击事件，大于的发返回false，不处理
                    if (upTime - downTime < 500) {
                        link[0].onClick(widget)
                    } else {
                        return false
                    }
                }
                return true
            } else {
                //添加富文本点击事件 和 对应的parentView 点击事件时，富文本的点击事件会拦截 TextView的父容器(ParentView)的点击事件；所以要手动调用
                //因为项目中文本有长按复制，也会和ClickSpan冲突，所以手动设置
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
                    val upTime = System.currentTimeMillis()
                    if (upTime - downTime < 500) {
                        if (parent is ViewGroup) {
                            parent.performClick()
                        }
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