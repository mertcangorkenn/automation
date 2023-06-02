 
package com.buzbuz.smartautoclicker.feature.scenario.debugging.ui.overlay

import android.app.Application
import android.content.SharedPreferences
import android.graphics.Rect

import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.feature.scenario.debugging.domain.DebuggingRepository
import com.buzbuz.smartautoclicker.feature.scenario.debugging.getDebugConfigPreferences
import com.buzbuz.smartautoclicker.feature.scenario.debugging.getIsDebugViewEnabled
import com.buzbuz.smartautoclicker.feature.scenario.debugging.ui.report.formatConfidenceRate

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/** ViewModel for the debug features. */
@OptIn(ExperimentalCoroutinesApi::class)
class DebugModel(application: Application) : AndroidViewModel(application) {

    /** Debug configuration shared preferences. */
    private val sharedPreferences: SharedPreferences = application.getDebugConfigPreferences()
    /** The debugging engine. */
    private var repository: DebuggingRepository = DebuggingRepository.getDebuggingRepository(application)

    /** Tells if the current detection is running in debug mode. */
    val isDebugging = repository.isDebugging.map { debugging ->
        debugging && sharedPreferences.getIsDebugViewEnabled(application)
    }

    /** The coordinates of the last positive detection. */
    val debugLastPositiveCoordinates: Flow<Rect> = repository.lastResult
        .map { debugInfo ->
            if (debugInfo != null && debugInfo.detectionResult.isDetected) debugInfo.conditionArea
            else Rect()
        }

    /** The info on the last positive detection. */
    val debugLastPositive: Flow<LastPositiveDebugInfo> = repository.lastPositiveInfo
        .flatMapLatest { debugInfo ->
            flow {
                if (debugInfo == null) {
                    emit(LastPositiveDebugInfo())
                    return@flow
                }

                emit(
                    LastPositiveDebugInfo(
                        debugInfo.event.name,
                        debugInfo.condition.name,
                        debugInfo.detectionResult.confidenceRate.formatConfidenceRate(),
                    )
                )

                delay(POSITIVE_VALUE_DISPLAY_TIMEOUT_MS)
                emit(LastPositiveDebugInfo())
            }
        }

    private val _isDebugReportConsumed = MutableStateFlow(false)
    /** True if the debug report have been consumed, false if not. */
    private val isDebugReportConsumed: StateFlow<Boolean> = _isDebugReportConsumed

    /** True when a debug report is available. */
    val isDebugReportReady: Flow<Boolean> = repository.debugReport
        .combine(isDebugReportConsumed) { debugReport, consumed ->
            if (debugReport == null) {
                _isDebugReportConsumed.value = false
                false
            } else {
                !consumed
            }
        }

    /** Consume the current debug report. */
    fun consumeDebugReport() {
        _isDebugReportConsumed.value = true
    }
}

/**
 * Info on the last positive detection.
 * @param eventName name of the event
 * @param conditionName the name of the condition detected.
 * @param confidenceRateText the text to display for the confidence rate
 */
data class LastPositiveDebugInfo(
    val eventName: String = "",
    val conditionName: String = "",
    val confidenceRateText: String = "",
)

/** Delay before removing the last positive result display in debug. */
private const val POSITIVE_VALUE_DISPLAY_TIMEOUT_MS = 1500L