 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.activities

import android.app.Application
import android.content.Intent

import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.core.queryIntentActivitiesCompat
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.getActivityDisplayInfo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * View model for the [ActivitySelectionDialog].
 * @param application the Android application.
 */
class ActivitySelectionModel(application: Application) : AndroidViewModel(application) {

    /** Retrieves the list of activities visible on the Android launcher. */
    val activities = flow {
        val resolveInfoList = application.packageManager.queryIntentActivitiesCompat(
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
            0,
        )

        emit(
            resolveInfoList
                .mapNotNull { it.getActivityDisplayInfo(application.packageManager) }
                .sortedBy { it.name.lowercase() }
        )
    }.flowOn(Dispatchers.IO)
}