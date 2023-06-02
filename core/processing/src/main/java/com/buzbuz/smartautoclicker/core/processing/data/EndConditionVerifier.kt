 
package com.buzbuz.smartautoclicker.core.processing.data

import com.buzbuz.smartautoclicker.core.domain.model.ConditionOperator
import com.buzbuz.smartautoclicker.core.domain.model.AND
import com.buzbuz.smartautoclicker.core.domain.model.OR
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.core.domain.model.endcondition.EndCondition

/**
 * Verifies if the scenario has reached its end conditions.
 *
 * @param conditions the list of the scenario end conditions.
 * @param conditionOperator the operator to apply to the conditions.
 * @param onEndConditionReached called when the scenario end conditions are fulfilled.
 */
internal class EndConditionVerifier(
    conditions: List<EndCondition>,
    @ConditionOperator private val conditionOperator: Int,
    private val onEndConditionReached: () -> Unit,
) {

    /** State of the executed events. EventId to Execution info.*/
    private val executedEvents = HashMap<Long, ExecutionInfo>().apply {
        conditions.forEach { condition ->
            put(condition.eventId!!.databaseId, ExecutionInfo(maxExecutionCount = condition.executions))
        }
    }
    /** The list of already completed conditions. Used only for [AND]. */
    private val completedConditions: MutableList<Long> = mutableListOf()

    /** The execution info for the currently triggered event. */
    private var triggeredEventInfo = ExecutionInfo()

    /**
     * Called when a event is triggered.
     * Increment the execution count for the event and verify if the end conditions are fulfilled.
     *
     * @param event the event triggered.
     * @return true if the end conditions are fulfilled, false if not.
     */
    fun onEventTriggered(event: Event): Boolean {
        // Is the event has a end condition ? If not, return false.
        triggeredEventInfo = executedEvents[event.id.databaseId] ?: return false

        // Increment the execution count and verify if this event end condition is fulfilled. If not, return false.
        triggeredEventInfo.executionCount++
        if (!triggeredEventInfo.conditionIsFulfilled()) return false

        // If the operator is OR, we only need one condition fulfilled, notify end and return true.
        if (conditionOperator == OR) {
            onEndConditionReached()
            return true
        }

        // If the operator is AND, add this end condition as reached, and verify if all conditions are now reached.
        completedConditions.add(event.id.databaseId)
        if (completedConditions.size == executedEvents.size) {
            onEndConditionReached()
            return true
        }
        return false
    }
}

/**
 * Execution information for an event.
 *
 * @param executionCount the number of execution for this event.
 * @param maxExecutionCount the number of execution to reach to fulfill the end condition.
 */
private data class ExecutionInfo(
    var executionCount: Int = 0,
    val maxExecutionCount: Int = 0,
) {

    /** @return true if the executionCount has reached the maxExecutionCount, false if not. */
    fun conditionIsFulfilled(): Boolean = executionCount >= maxExecutionCount
}