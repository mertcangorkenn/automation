 
package com.buzbuz.smartautoclicker

import android.app.Application
import com.google.android.material.color.DynamicColors

class SmartAutoClickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}