 
@file:Suppress("DEPRECATION")
package com.buzbuz.smartautoclicker.core.display

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.Surface
import android.view.WindowManager

/**
 * Provides metrics for the screen such as orientation or display size.
 * In order for the metrics to be updated upon screen rotation, you must call [startMonitoring] first. Once the metrics
 * are no longer needed, call [stopMonitoring] to release all resources.
 *
 * @param context the Android context.
 */
class DisplayMetrics internal constructor(context: Context) {

    companion object {
        /** WindowManager LayoutParams type for a window over applications. */
        @JvmField
        val TYPE_COMPAT_OVERLAY =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE


        /** Singleton preventing multiple instances at the same time. */
        @Volatile
        private var INSTANCE: DisplayMetrics? = null

        /**
         * Get the ScreenMetrics singleton, or instantiates it if it wasn't yet.
         *
         * @param context the Android context.
         *
         * @return the ScreenMetrics singleton.
         */
        fun getInstance(context: Context): DisplayMetrics {
            return INSTANCE ?: synchronized(this) {
                val instance = DisplayMetrics(context)
                INSTANCE = instance
                instance
            }
        }
    }

    /** The Android window manager. */
    private val windowManager = context.getSystemService(WindowManager::class.java)
    /** The Android display manager. */
    private val displayManager = context.getSystemService(DisplayManager::class.java)
    /** The display to get the value from. It will always be the first one available. */
    private val display = displayManager.getDisplay(0)

    /** The listeners upon orientation changes. */
    private val orientationListeners: MutableSet<((Context) -> Unit)> = mutableSetOf()

    /** Listen to the configuration changes and calls [orientationListeners] when needed. */
    private val configChangedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (updateScreenConfig()) {
                orientationListeners.forEach { it.invoke(context) }
            }
        }
    }

    /** The orientation of the display. */
    var orientation: Int = Configuration.ORIENTATION_UNDEFINED
        private set
    /** The screen size. */
    var screenSize: Point = Point(0, 0)
        private set

    init {
        updateScreenConfig()
    }

    /** Start the monitoring of the screen metrics. */
    fun startMonitoring(context: Context) {
        context.registerReceiver(configChangedReceiver, IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED))
    }

    /** Stop the monitoring of the screen metrics. All listeners will be unregistered. */
    fun stopMonitoring(context: Context) {
        context.unregisterReceiver(configChangedReceiver)
        orientationListeners.clear()
    }

    /**
     * Register a new orientation listener.
     *
     * @param listener the listener to be registered.
     */
    fun addOrientationListener(listener: (Context) -> Unit) {
        orientationListeners.add(listener)
    }

    /** Unregister a previously registered listener. */
    fun removeOrientationListener(listener: (Context) -> Unit) {
        orientationListeners.remove(listener)
    }

    /** @return true if the screen config have changed, false if not. */
    private fun updateScreenConfig(): Boolean {
        val newOrientation = getCurrentOrientation()
        if (newOrientation == orientation) return false

        val newSize = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.bounds.let { windowBound ->
                Point(windowBound.width(), windowBound.height())
            }
        } else {
            val realSize = Point()
            display.getRealSize(realSize)
            realSize
        }

        orientation = newOrientation
        screenSize = if (newSize == screenSize) {
            Point(newSize.y, newSize.x)
        } else {
            newSize
        }

        return true
    }

    /**  @return the orientation of the screen. */
    private fun getCurrentOrientation(): Int = when (display.rotation) {
        Surface.ROTATION_0, Surface.ROTATION_180 -> Configuration.ORIENTATION_PORTRAIT
        Surface.ROTATION_90, Surface.ROTATION_270 -> Configuration.ORIENTATION_LANDSCAPE
        else -> Configuration.ORIENTATION_UNDEFINED
    }
}