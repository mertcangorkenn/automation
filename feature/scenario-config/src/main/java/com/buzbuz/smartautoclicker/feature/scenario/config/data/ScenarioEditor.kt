 
package com.buzbuz.smartautoclicker.feature.scenario.config.data

import com.buzbuz.smartautoclicker.core.domain.model.endcondition.EndCondition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.core.domain.model.scenario.Scenario
import com.buzbuz.smartautoclicker.feature.scenario.config.data.base.ListEditor
import com.buzbuz.smartautoclicker.feature.scenario.config.domain.EditedElementState
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine

internal class ScenarioEditor {

    private val referenceScenario: MutableStateFlow<Scenario?> = MutableStateFlow(null)
    private val _editedScenario: MutableStateFlow<Scenario?> = MutableStateFlow((null))

    val editedScenario: StateFlow<Scenario?> = _editedScenario
    val editedScenarioState: Flow<EditedElementState<Scenario>> = combine(referenceScenario, _editedScenario) { ref, edit ->
        val hasChanged =
            if (ref == null || edit == null) false
            else ref != edit

        EditedElementState(edit, hasChanged, true)
    }

    val eventsEditor = EventsEditor(::deleteAllReferencesToEvent)
    val endConditionsEditor = object : ListEditor<EndCondition>(canBeEmpty = true) {
        override fun areItemsTheSame(a: EndCondition, b: EndCondition): Boolean = a.id == b.id
        override fun isItemComplete(item: EndCondition): Boolean = item.isComplete()
    }

    fun startEdition(scenario: Scenario, events: List<Event>, endConditions: List<EndCondition>) {
        referenceScenario.value = scenario
        _editedScenario.value = scenario

        eventsEditor.startEdition(events)
        endConditionsEditor.startEdition(endConditions)
    }

    fun stopEdition() {
        endConditionsEditor.stopEdition()
        eventsEditor.stopEdition()

        referenceScenario.value = null
        _editedScenario.value = null
    }

    fun updateEditedScenario(item: Scenario) {
        _editedScenario.value ?: return
        _editedScenario.value = item
    }

    private fun deleteAllReferencesToEvent(event: Event) {
        eventsEditor.deleteAllActionsReferencing(event)

        endConditionsEditor.editedList.value
            ?.filter { it.eventId != event.id }
            ?.let { endConditionsEditor.updateList(it) }

    }
}