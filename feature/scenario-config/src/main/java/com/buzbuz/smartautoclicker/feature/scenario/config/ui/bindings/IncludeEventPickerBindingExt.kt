 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.bindings

import android.view.View

import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.IncludeEventPickerBinding

/** Update the ui state of the selected event. */
fun IncludeEventPickerBinding.updateState(
    viewState: EventPickerViewState,
    onPickerClicked: (List<Event>) -> Unit,
) {
    when (viewState) {
        EventPickerViewState.NoEvents -> {
            eventNone.visibility = View.VISIBLE
            eventEmpty.visibility = View.GONE
            includeSelectedEvent.root.visibility = View.GONE
        }

        is EventPickerViewState.NoSelection -> {
            eventNone.visibility = View.GONE
            eventEmpty.visibility = View.VISIBLE
            eventEmpty.setOnClickListener { onPickerClicked(viewState.availableEvents) }
            includeSelectedEvent.root.visibility = View.GONE
        }

        is EventPickerViewState.Selected -> {
            eventNone.visibility = View.GONE
            eventEmpty.visibility = View.GONE
            includeSelectedEvent.root.visibility = View.VISIBLE
            includeSelectedEvent.bind(viewState.event, false) { onPickerClicked(viewState.availableEvents) }
        }
    }
}

/** Possible states for a [IncludeEventPickerBinding]. */
sealed class EventPickerViewState {
    object NoEvents : EventPickerViewState()
    data class NoSelection(val availableEvents: List<Event>): EventPickerViewState()
    data class Selected(val event: Event, val availableEvents: List<Event>): EventPickerViewState()
}