 
package com.buzbuz.smartautoclicker.feature.scenario.debugging.domain

import android.graphics.Rect

import com.buzbuz.smartautoclicker.core.detection.DetectionResult
import com.buzbuz.smartautoclicker.core.domain.model.condition.Condition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event

/** Debug information for the scenario processing */
data class DebugInfo(
    val event: Event,
    val condition: Condition,
    val detectionResult: DetectionResult,
    val conditionArea: Rect,
)