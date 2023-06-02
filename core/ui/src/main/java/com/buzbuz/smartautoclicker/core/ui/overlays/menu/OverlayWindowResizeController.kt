 
package com.buzbuz.smartautoclicker.core.ui.overlays.menu

import android.animation.LayoutTransition
import android.util.Size
import android.view.View
import android.view.ViewGroup

/**
 * Controls the resize of an overlay window.
 *
 * As the root view of an overlay window can't be animated during a resize trigger automatically via the xml attribute
 * animateLayoutChanges, we need to manually handle it.
 * To fix this issue, we use a [maximumSize] for our window, which will be the biggest width and height it can take in
 * all ui states. Before a size transition, the window will be set to this maximum value, allowing the
 * [backgroundViewGroup] and it's content to be correctly animated. Once the auto resize animation is completed, the
 * window size is set back to the size of it's content.
 * For obvious reason, the root view of the window should be transparent, and should only contain one view, the
 * [backgroundViewGroup].
 *
 * @param backgroundViewGroup the view displaying the background of the overlay
 * @param resizedContainer the view originally resized and that triggers the window resizing.
 * @param maximumSize the maximum width and height the window can have between all its states.
 * @param windowSizeListener called when the window needs to be resized.
 */
internal class OverlayWindowResizeController(
    private val backgroundViewGroup: ViewGroup,
    private val resizedContainer: ViewGroup,
    private val maximumSize: Size,
    private val windowSizeListener: (size: Size) -> Unit,
) {

    /** True if the window resize animation is running, false if not. */
    var isAnimating: Boolean = false
        private set

    /** Monitor the transitions triggered by animateLayoutChanges on the [resizedContainer]. */
    private val transitionListener = object : LayoutTransition.TransitionListener {

        override fun startTransition(
            transition: LayoutTransition?,
            container: ViewGroup?,
            view: View?,
            transitionType: Int
        ) {}

        override fun endTransition(
            transition: LayoutTransition?,
            container: ViewGroup?,
            view: View?,
            transitionType: Int
        ) {
            if (view != null && view.id == backgroundViewGroup.id && isChangeTransition(transitionType)) {
                // The view resize animation is over, restore the window size to wrap the content.
                windowSizeListener(Size(backgroundViewGroup.measuredWidth, backgroundViewGroup.measuredHeight))
                isAnimating = false
            }
        }
    }

    init {
        resizedContainer.layoutTransition?.addTransitionListener(transitionListener)
    }

    /**
     * Call this method when doing any view changes that will resize the window.
     * Setup the window size and execute the changes.
     */
    fun animateLayoutChanges(layoutChanges: () -> Unit) {
        if (isAnimating) return
        isAnimating = true

        // Freeze window size to expanded size
        windowSizeListener(maximumSize)

        // Execute layout changes that will cause a resize
        layoutChanges()
    }

    /** Release this controller. */
    fun release() {
        resizedContainer.layoutTransition?.removeTransitionListener(transitionListener)
    }

    /** True if the transition [transitionType] is a resize transition. */
    private fun isChangeTransition(transitionType: Int): Boolean =
        transitionType == LayoutTransition.CHANGING ||
                transitionType == LayoutTransition.CHANGE_APPEARING ||
                transitionType == LayoutTransition.CHANGE_DISAPPEARING
}