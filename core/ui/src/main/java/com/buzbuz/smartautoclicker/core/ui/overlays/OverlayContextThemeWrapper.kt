 
package com.buzbuz.smartautoclicker.core.ui.overlays

import android.content.Context
import android.content.res.Configuration

import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper

import com.google.android.material.color.DynamicColors

/**
 * Create a new context theme wrapper from an Android Application Context.
 *
 * As a context not attached to an Activity doesn't receive UI configuration updates or theme information, it can't be
 * used with some Android UI APIs such as a dialog. To fix this, we need to wrap the application context with a
 * [ContextThemeWrapper] and update its configuration manually with the correct UI information.
 *
 * @param applicationContext the Android Application Context.
 * @param theme the theme to use for this context.
 * @param currentOrientation the current device screen orientation.
 */
internal fun newOverlayContextThemeWrapper(
    applicationContext: Context,
    @StyleRes theme: Int,
    currentOrientation: Int,
) : Context = DynamicColors.wrapContextIfAvailable(
    ContextThemeWrapper(applicationContext, theme).apply {
        applyOverrideConfiguration(
            Configuration(applicationContext.resources.configuration).apply {
                orientation = currentOrientation
            }
        )
    }
)