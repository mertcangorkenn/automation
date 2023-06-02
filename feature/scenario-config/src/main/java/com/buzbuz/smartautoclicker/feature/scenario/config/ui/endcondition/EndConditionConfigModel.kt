 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.endcondition

import android.app.Application

import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.core.domain.model.endcondition.EndCondition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.feature.scenario.config.domain.EditionRepository
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.bindings.EventPickerViewState

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.take

/**
 * View model for the [EndConditionConfigDialog].
 *
 * @param application the Android application.
 */
class EndConditionConfigModel(application: Application) : AndroidViewModel(application) {

    /** Maintains the currently configured scenario state. */
    private val editionRepository = EditionRepository.getInstance(application)
    /** Currently configured end conditions. */
    private val editedEndCondition: Flow<EndCondition> = editionRepository.editionState.editedEndConditionState
        .mapNotNull { it.value }

    /** The number of executions before triggering the end condition. */
    val executions = editedEndCondition
        .filterNotNull()
        .map { it.executions }
        .take(1)
    /** Tells if the execution count is valid or not. */
    val executionCountError: Flow<Boolean> = editedEndCondition
        .map { it.executions <= 0 }

    /** True if this end condition is valid and can be saved, false if not. */
    val endConditionCanBeSaved: Flow<Boolean> = editionRepository.editionState.editedEndConditionState
        .map { it.canBeSaved }

    /** The event selected for the end condition. Null if none is. */
    val eventViewState: Flow<EventPickerViewState> = editionRepository.editionState.eventsState
        .combine(editedEndCondition) { events, endCondition ->
            events.value?.find { event -> endCondition.eventId == event.id }
        }
        .combine(editionRepository.editionState.eventsAvailableForNewEndCondition) { selectedEvent, events ->
            when {
                selectedEvent != null -> EventPickerViewState.Selected(selectedEvent, events)
                events.isEmpty() -> EventPickerViewState.NoEvents
                else -> EventPickerViewState.NoSelection(events)
            }
        }

    /**
     * Set the event for the configured end condition
     * @param event the new event.
     */
    fun setEvent(event: Event) {
        editionRepository.editionState.getEditedEndCondition()?.let { endCondition ->
            editionRepository.updateEditedEndCondition(
                endCondition.copy(
                    eventId = event.id,
                    eventName = event.name,
                )
            )
        }
    }

    /**
     * Set the number of execution for the linked event.
     * @param executions the number of event executions.
     */
    fun setExecutions(executions: Int) {
        editionRepository.editionState.getEditedEndCondition()?.let { endCondition ->
            editionRepository.updateEditedEndCondition(
                endCondition.copy(
                    executions = executions
                )
            )
        }
    }
}