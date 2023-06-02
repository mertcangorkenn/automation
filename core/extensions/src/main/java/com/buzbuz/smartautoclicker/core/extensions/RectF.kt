 
package com.buzbuz.smartautoclicker.core

import android.graphics.PointF
import android.graphics.RectF

/**
 * Scale this RectF around the given pivot.
 *
 * @param scaleFactor the scale ratio, < 1 to scale down, > 1 to scale up.
 * @param pivot the point around which the "zoom in/out" effect occurs. Use center to scale equally all borders.
 */
fun RectF.scale(scaleFactor: Float, pivot: PointF) {
    val widthOffset = (width() * scaleFactor - width())
    val heightOffset = (height() * scaleFactor - height())

    val pivotRatioX = (pivot.x - left) / width()
    val pivotRatioY = (pivot.y - top) / height()

    left -= widthOffset * pivotRatioX
    top -= heightOffset * pivotRatioY
    right += widthOffset * (1 - pivotRatioX)
    bottom += heightOffset * (1 - pivotRatioY)
}

/**
 * Translate this RectF with the given values.
 *
 * @param translateX the value to be added to left and right. < 0 to go left, > 0 to go right.
 * @param translateY the value to be added to top and bottom. < 0 to go up, > 0 to go down.
 */
fun RectF.translate(translateX: Float, translateY: Float) {
    left += translateX
    top += translateY
    right += translateX
    bottom += translateY
}
