package com.flyjingfish.formattextview

class Gradient(
    var gradientColors: IntArray,
    var gradientPositions: FloatArray?,
    var orientation: Orientation?
) {
    enum class Orientation{
        LEFT_TO_RIGHT,
        TOP_TO_BOTTOM,
        LEFT_TOP_TO_RIGHT_BOTTOM,
        LEFT_BOTTOM_TO_RIGHT_TOP
    }
}