 
package com.buzbuz.smartautoclicker.core.ui.testutils

import android.view.View
import android.view.WindowManager

import org.junit.Assert.assertNotNull

import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

/**
 * Holder class for the parameters of [WindowManager.addView].
 * @param view the view added to the window manager
 * @param params the layout params for this view.
 */
data class WmAddedView(
    val view: View,
    val params: WindowManager.LayoutParams
)

/**
 * Capture the parameters for a single call to [WindowManager.addView].
 * @param mockWindowManager the window manager that will add the views
 * @return the [WindowManager.addView] method parameters.
 */
fun captureWindowManagerAddedMenuView(mockWindowManager: WindowManager) : WmAddedView {
    val wmAddViewCaptor = ArgumentCaptor.forClass(View::class.java)
    val wmAddViewParamsCaptor = ArgumentCaptor.forClass(WindowManager.LayoutParams::class.java)
    verify(mockWindowManager).addView(wmAddViewCaptor.capture(), wmAddViewParamsCaptor.capture())

    assertNotNull("Can't get menu view, it is null", wmAddViewCaptor.value)
    assertNotNull("Can't get menu view layout params, they are null", wmAddViewParamsCaptor.value)

    return WmAddedView(wmAddViewCaptor.value, wmAddViewParamsCaptor.value)
}

/**
 * Capture the parameters for two calls to [WindowManager.addView].
 * @param mockWindowManager the window manager that will add the views
 * @return the [WindowManager.addView] method parameters.
 */
fun captureWindowManagerAddedViews(mockWindowManager: WindowManager) : Pair<WmAddedView, WmAddedView> {
    val wmAddViewCaptor = ArgumentCaptor.forClass(View::class.java)
    val wmAddViewParamsCaptor = ArgumentCaptor.forClass(WindowManager.LayoutParams::class.java)
    verify(mockWindowManager, times(2))
        .addView(wmAddViewCaptor.capture(), wmAddViewParamsCaptor.capture())

    assertNotNull("Can't get menu view, it is null", wmAddViewCaptor.allValues[0])
    assertNotNull("Can't get menu view layout params, they are null", wmAddViewParamsCaptor.allValues[0])
    assertNotNull("Can't get overlay view, it is null", wmAddViewCaptor.allValues[1])
    assertNotNull("Can't get overlay view layout params, they are null", wmAddViewParamsCaptor.allValues[1])

    return WmAddedView(wmAddViewCaptor.allValues[0], wmAddViewParamsCaptor.allValues[0]) to
            WmAddedView(wmAddViewCaptor.allValues[1], wmAddViewParamsCaptor.allValues[1])
}
