 
package com.buzbuz.smartautoclicker.core.domain.model.endcondition

import androidx.annotation.IntRange

import com.buzbuz.smartautoclicker.core.domain.model.Identifier

/**
 * End condition for a scenario.
 *
 * @param id the unique identifier for the end condition.
 * @param scenarioId the unique identifier of the scenario for this end condition.
 * @param eventId the unique identifier of the event associated with this end condition.
 * @param eventName the name of the associated event.
 * @param executions the number of execution of the associated event before fulfilling this end condition.
 */
data class EndCondition(
    val id: Identifier,
    val scenarioId: Identifier,
    val eventId: Identifier? = null,
    val eventName: String? = null,
    @IntRange(from = 1) val executions: Int = 1,
) {

    /** @return true if this end condition is complete and can be transformed into its entity. */
    fun isComplete(): Boolean =
        eventId != null && eventName != null
}