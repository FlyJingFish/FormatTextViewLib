package com.flyjingfish.formattextview

import android.view.View

interface OnFormatClickListener {
    /**
     * 每一个富文本点击回调
     */
    fun onItemClick(position: Int)

    /**
     * 非富文本点击回调
     */
    fun onNoItemClick(view : View)
}