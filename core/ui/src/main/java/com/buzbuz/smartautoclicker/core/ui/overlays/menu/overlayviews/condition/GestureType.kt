 
package com.buzbuz.smartautoclicker.core.ui.overlays.menu.overlayviews.condition

import android.graphics.RectF

/** Types of gesture for the [Selector]. */
internal sealed class GestureType {
    /**
     * Get the touch area for this gesture.
     *
     * @param viewArea the area of the selector
     * @param handleSize the size of the view handle
     * @param innerHandleSize the inner size of the handle.
     */
    open fun getGestureArea(viewArea: RectF, handleSize: Float, innerHandleSize: Float): RectF =
        viewArea

    /**
     * Tells if the selector is big enough to consider using part of it as gesture area.
     *
     * @param viewArea the area of the view
     * @param handleSize the size of the handle.
     * @return true if you can use part of the selector as gesture area, false if not.
     */
    fun isEnoughInnerSpace(viewArea: RectF, handleSize: Float) =
        viewArea.height() > handleSize && viewArea.width() > handleSize

}

internal object ResizeLeft: GestureType() {
    override fun getGestureArea(viewArea: RectF, handleSize: Float, innerHandleSize: Float) =
        RectF(
            viewArea.left - handleSize,
            viewArea.top,
            if(isEnoughInnerSpace(viewArea, handleSize)) viewArea.left + innerHandleSize else viewArea.left,
            viewArea.bottom
        )
}

internal object ResizeTop: GestureType() {
    override fun getGestureArea(viewArea: RectF, handleSize: Float, innerHandleSize: Float): RectF =
        RectF(
            viewArea.left,
            viewArea.top  - handleSize,
            viewArea.right,
            if (isEnoughInnerSpace(viewArea, handleSize)) viewArea.top + innerHandleSize else viewArea.top
        )
}

internal object ResizeRight: GestureType() {
    override fun getGestureArea(viewArea: RectF, handleSize: Float, innerHandleSize: Float): RectF =
        RectF(
            if(isEnoughInnerSpace(viewArea, handleSize)) viewArea.right - innerHandleSize else viewArea.right,
            viewArea.top,
            viewArea.right + handleSize,
            viewArea.bottom
        )
}

internal object ResizeBottom: GestureType() {
    override fun getGestureArea(viewArea: RectF, handleSize: Float, innerHandleSize: Float): RectF =
        RectF(
            viewArea.left,
            if (isEnoughInnerSpace(viewArea, handleSize)) viewArea.bottom - innerHandleSize else viewArea.bottom,
            viewArea.right,
            viewArea.bottom + handleSize
        )
}

internal object MoveSelector: GestureType()

internal object ZoomCapture: GestureType()