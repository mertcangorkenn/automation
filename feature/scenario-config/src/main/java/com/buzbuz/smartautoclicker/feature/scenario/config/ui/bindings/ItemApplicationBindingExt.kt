 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.bindings

import android.content.ComponentName

import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.ItemApplicationBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.ActivityDisplayInfo

/** Binds to the provided activity. */
fun ItemApplicationBinding.bind(activity: ActivityDisplayInfo, listener: ((ComponentName) -> Unit)? = null) {
    textApp.text = activity.name
    iconApp.setImageDrawable(activity.icon)

    listener?.let { root.setOnClickListener { it(activity.componentName) } }
}