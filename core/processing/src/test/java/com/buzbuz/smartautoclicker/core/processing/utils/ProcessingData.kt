 
package com.buzbuz.smartautoclicker.core.processing.utils

import android.graphics.Rect

import com.buzbuz.smartautoclicker.core.domain.model.AND
import com.buzbuz.smartautoclicker.core.domain.model.ConditionOperator
import com.buzbuz.smartautoclicker.core.domain.model.DetectionType
import com.buzbuz.smartautoclicker.core.domain.model.Identifier
import com.buzbuz.smartautoclicker.core.domain.model.action.Action
import com.buzbuz.smartautoclicker.core.domain.model.condition.Condition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event

/** Test data and helpers for the detection tests. */
internal object ProcessingData {

    /** Instantiates a new event with only the useful values for the tests. */
    fun newEvent(
        id: Long = 1L,
        @ConditionOperator operator: Int = AND,
        actions: List<Action> = emptyList(),
        conditions: List<Condition> = emptyList(),
        enableOnStart: Boolean = true,
    ) = Event(
        id = Identifier(id),
        scenarioId = Identifier(1L),
        name = "TOTO",
        conditionOperator = operator,
        priority = 0,
        actions = actions.toMutableList(),
        conditions = conditions.toMutableList(),
        enabledOnStart = enableOnStart,
    )

    /** Instantiates a new condition with only the useful values for the tests. */
    fun newCondition(
        path: String,
        area: Rect,
        threshold: Int,
        @DetectionType detectionType: Int,
        shouldBeDetected: Boolean = true,
    ) = Condition(
        Identifier(1L),
        Identifier(1L),
        "TOTO",
        path,
        area,
        threshold,
        detectionType,
        shouldBeDetected,
        null
    )
}