 
package com.buzbuz.smartautoclicker.core.ui.overlays.menu.overlayviews.condition

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent

import androidx.annotation.CallSuper

import com.buzbuz.smartautoclicker.core.display.DisplayMetrics

/**
 * Base class for all view components displayed in the [ConditionSelectorView].
 *
 * @param displayMetrics provides information about current display.
 * @param viewInvalidator calls invalidate on the view hosting this component.
 */
internal abstract class ViewComponent(
    private val displayMetrics: DisplayMetrics,
    private val viewInvalidator: () -> Unit,
) {

    /** The maximum size of the selector. */
    protected val maxArea: RectF = RectF().apply {
        val screenSize = displayMetrics.screenSize
        right = screenSize.x.toFloat()
        bottom = screenSize.y.toFloat()
    }

    /**
     * Called when the size of the [ConditionSelectorView] have changed.
     * Update the maximum area. Can be overridden to clear/adjust the displayed component position.
     *
     * @param w the width of the new view.
     * @param h the height of the new view.
     */
    @CallSuper
    open fun onViewSizeChanged(w: Int, h: Int) {
        val screenSize = displayMetrics.screenSize
        maxArea.apply {
            right = screenSize.x.toFloat()
            bottom = screenSize.y.toFloat()
        }
    }

    /**
     * Called when a touch event occurs in the [ConditionSelectorView].
     *
     * @param event the new touch event.
     * @return true if the event has been consumed, false if not.
     */
    abstract fun onTouchEvent(event: MotionEvent): Boolean

    /**
     * Called when the view needs to draw this component.
     *
     * @param canvas the canvas to draw in.
     */
    abstract fun onDraw(canvas: Canvas)

    /**
     * Called when this components needs to be reset (like after a cancel).
     * All temporary values should be dropped and the component should returns to its initial state.
     */
    abstract fun onReset()

    /** Invalidates the view containing the component. */
    @CallSuper
    protected open fun invalidate() = viewInvalidator()
}