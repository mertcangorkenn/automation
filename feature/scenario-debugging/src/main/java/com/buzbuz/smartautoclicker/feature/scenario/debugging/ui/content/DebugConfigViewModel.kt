 
package com.buzbuz.smartautoclicker.feature.scenario.debugging.ui.content

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.feature.scenario.debugging.getDebugConfigPreferences
import com.buzbuz.smartautoclicker.feature.scenario.debugging.getIsDebugReportEnabled
import com.buzbuz.smartautoclicker.feature.scenario.debugging.getIsDebugViewEnabled
import com.buzbuz.smartautoclicker.feature.scenario.debugging.putIsDebugReportEnabled
import com.buzbuz.smartautoclicker.feature.scenario.debugging.putIsDebugViewEnabled

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DebugConfigViewModel(application: Application) : AndroidViewModel(application) {

    /** Debug configuration shared preferences. */
    private val sharedPreferences: SharedPreferences = application.getDebugConfigPreferences()

    /** Tells if the debug view is enabled or not. */
    private val _isDebugViewEnabled = MutableStateFlow(sharedPreferences.getIsDebugViewEnabled(application))
    val isDebugViewEnabled: Flow<Boolean> = _isDebugViewEnabled

    /** Tells if the debug report is enabled or not. */
    private val _isDebugReportEnabled = MutableStateFlow(sharedPreferences.getIsDebugReportEnabled(application))
    val isDebugReportEnabled: Flow<Boolean> = _isDebugReportEnabled

    fun toggleIsDebugViewEnabled() {
        _isDebugViewEnabled.value = !_isDebugViewEnabled.value
    }

    fun toggleIsDebugReportEnabled() {
        _isDebugReportEnabled.value = !_isDebugReportEnabled.value
    }

    fun saveConfig() {
        sharedPreferences
            .edit()
            .putIsDebugViewEnabled(_isDebugViewEnabled.value)
            .putIsDebugReportEnabled(_isDebugReportEnabled.value)
            .apply()
    }
}