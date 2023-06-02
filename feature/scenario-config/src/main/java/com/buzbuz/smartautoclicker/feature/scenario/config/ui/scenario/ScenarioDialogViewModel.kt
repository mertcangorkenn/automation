 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario

import android.app.Application

import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.domain.EditionRepository
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.NavigationViewModel

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/** ViewModel for the [ScenarioDialog] and its content. */
class ScenarioDialogViewModel(application: Application) : NavigationViewModel(application) {

    private val editionRepository: EditionRepository = EditionRepository.getInstance(application)

    /**
     * Tells if all content have their field correctly configured.
     * Used to display the red badge if indicating if there is something missing.
     */
    val navItemsValidity: Flow<Map<Int, Boolean>> = combine(
        editionRepository.editionState.eventsState.filterNotNull(),
        editionRepository.editionState.scenarioState.filterNotNull(),
    ) { eventListState, scenarioState ->
        buildMap {
            put(R.id.page_events, eventListState.canBeSaved)
        }
    }

    /** Tells if the configured scenario is valid and can be saved in database. */
    val scenarioCanBeSaved: Flow<Boolean> = editionRepository.editionState.scenarioCompleteState
        .map { it.canBeSaved }
}
