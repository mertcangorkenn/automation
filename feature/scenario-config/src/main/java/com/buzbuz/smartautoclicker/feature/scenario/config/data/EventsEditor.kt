 
package com.buzbuz.smartautoclicker.feature.scenario.config.data

import com.buzbuz.smartautoclicker.core.domain.model.action.Action
import com.buzbuz.smartautoclicker.core.domain.model.condition.Condition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.feature.scenario.config.data.base.ListEditor

class EventsEditor(private val onDeleteEvent: (Event) -> Unit): ListEditor<Event>() {

    override fun areItemsTheSame(a: Event, b: Event): Boolean = a.id == b.id
    override fun isItemComplete(item: Event): Boolean = item.isComplete()

    val conditionsEditor = object : ListEditor<Condition>(::onEditedEventConditionsUpdated, false) {
        override fun areItemsTheSame(a: Condition, b: Condition): Boolean = a.id == b.id
        override fun isItemComplete(item: Condition): Boolean = item.isComplete()
    }

    val actionsEditor = ActionsEditor(::onEditedEventActionsUpdated)

    override fun startItemEdition(item: Event) {
        super.startItemEdition(item)
        conditionsEditor.startEdition(item.conditions)
        actionsEditor.startEdition(item.actions)
    }

    override fun deleteEditedItem() {
        val editedItem = editedItem.value ?: return
        onDeleteEvent(editedItem)
        super.deleteEditedItem()
    }

    override fun stopItemEdition() {
        actionsEditor.stopEdition()
        conditionsEditor.stopEdition()
        super.stopItemEdition()
    }

    fun deleteAllActionsReferencing(event: Event) {
        val events = editedList.value ?: return

        val newEvents = events.mapNotNull { scenarioEvent ->
            if (scenarioEvent.id == event.id) return@mapNotNull null // Skip same item

            val newActions = scenarioEvent.actions.toMutableList()
            scenarioEvent.actions.forEach { action ->
                if (action is Action.ToggleEvent && action.toggleEventId == event.id) newActions.remove(action)
            }

            scenarioEvent.copy(actions = newActions)
        }

        updateList(newEvents)
    }

    private fun onEditedEventConditionsUpdated(conditions: List<Condition>) {
        _editedItem.value?.let { event ->
            updateEditedItem(event.copy(conditions = conditions))
        }
    }

    private fun onEditedEventActionsUpdated(actions: List<Action>) {
        _editedItem.value?.let { event ->
            updateEditedItem(event.copy(actions = actions))
        }
    }
}