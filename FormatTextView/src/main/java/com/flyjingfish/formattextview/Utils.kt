package com.flyjingfish.formattextview

import android.content.Context
import android.util.TypedValue

fun dp2px(context: Context,dp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
}
fun sp2px(context: Context,sp: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
}