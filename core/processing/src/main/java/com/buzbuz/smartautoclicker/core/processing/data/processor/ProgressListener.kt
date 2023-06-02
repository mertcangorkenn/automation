 
package com.buzbuz.smartautoclicker.core.processing.data.processor

import android.content.Context

import com.buzbuz.smartautoclicker.core.detection.DetectionResult
import com.buzbuz.smartautoclicker.core.domain.model.condition.Condition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.core.domain.model.scenario.Scenario

interface ProgressListener {

    fun onSessionStarted(context: Context, scenario: Scenario, events: List<Event>)

    fun onImageProcessingStarted()

    fun onEventProcessingStarted(event: Event)

    fun onConditionProcessingStarted(condition: Condition)

    fun onConditionProcessingCompleted(detectionResult: DetectionResult)

    suspend fun onEventProcessingCompleted(
        isEventMatched: Boolean,
        event: Event?,
        condition: Condition?,
        result: DetectionResult?,
    )

    fun onImageProcessingCompleted()

    suspend fun onSessionEnded()

    fun cancelCurrentProcessing()
}

internal suspend fun ProgressListener.onEventProcessingCompleted(result: ProcessorResult) =
    onEventProcessingCompleted(result.eventMatched, result.event, result.condition, result.detectionResult)