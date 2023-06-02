 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.event

import android.app.Application

import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.domain.EditionRepository
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.NavigationViewModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class EventDialogViewModel(application: Application) : NavigationViewModel(application) {

    /** Repository containing the user editions. */
    private val editionRepository = EditionRepository.getInstance(application)

    /**
     * Tells if all content have their field correctly configured.
     * Used to display the red badge if indicating if there is something missing.
     */
    val navItemsValidity: Flow<Map<Int, Boolean>> = combine(
        editionRepository.editionState.editedEventState,
        editionRepository.editionState.editedEventConditionsState,
        editionRepository.editionState.editedEventActionsState,
    ) { editedEvent, conditions, actions, ->
        buildMap {
            put(R.id.page_conditions, conditions.canBeSaved)
            put(R.id.page_actions, actions.canBeSaved)
        }
    }

    /** Tells if the configured event is valid and can be saved. */
    val eventCanBeSaved: Flow<Boolean> = editionRepository.editionState.editedEventState
        .map { it.canBeSaved }

    /** Tells if this event have associated end conditions. */
    fun isEventHaveRelatedEndConditions(): Boolean =
        editionRepository.editionState.isEditedEventReferencedByEndCondition()

    /** Tells if this event have associated actions. */
    fun isEventHaveRelatedActions(): Boolean =
        editionRepository.editionState.isEditedEventReferencedByAction()
}