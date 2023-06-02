 
package com.buzbuz.smartautoclicker.feature.scenario.debugging.domain

import com.buzbuz.smartautoclicker.core.domain.model.condition.Condition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.core.domain.model.scenario.Scenario

data class DebugReport internal constructor(
    val scenario: Scenario,
    val sessionInfo: ProcessingDebugInfo,
    val imageProcessedInfo: ProcessingDebugInfo,
    val eventsTriggeredCount: Long,
    val eventsProcessedInfo: List<Pair<Event, ProcessingDebugInfo>>,
    val conditionsDetectedCount: Long,
    val conditionsProcessedInfo: Map<Long, Pair<Condition, ConditionProcessingDebugInfo>>,
)
