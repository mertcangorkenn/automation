 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.eventlist

import android.app.Application
import android.content.Context

import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.feature.billing.IBillingRepository
import com.buzbuz.smartautoclicker.feature.billing.ProModeAdvantage
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.core.domain.Repository
import com.buzbuz.smartautoclicker.feature.scenario.config.domain.EditionRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull

class EventListViewModel(application: Application) : AndroidViewModel(application) {

    /** The repository of the application. */
    private val repository: Repository = Repository.getRepository(application)
    /** Maintains the currently configured scenario state. */
    private val editionRepository = EditionRepository.getInstance(application)
    /** The repository for the pro mode billing. */
    private val billingRepository = IBillingRepository.getRepository(application)

    /** Currently configured events. */
    val eventsItems = editionRepository.editionState.eventsState
        .mapNotNull { it.value }

    /** Tells if the limitation in event count have been reached. */
    val isEventLimitReached: Flow<Boolean> = billingRepository.isProModePurchased
        .combine(eventsItems) { isProModePurchased, events ->
            !isProModePurchased && events.size >= ProModeAdvantage.Limitation.EVENT_COUNT_LIMIT.limit
        }
    /** Tells if the pro mode billing flow is being displayed. */
    val isBillingFlowDisplayed: Flow<Boolean> = billingRepository.isBillingFlowInProcess

    /** Tells if the copy button should be visible or not. */
    val copyButtonIsVisible: Flow<Boolean> =
        combine(repository.getAllEvents(), editionRepository.editionState.eventsState) { allEvts, scenarioEvts ->
            allEvts.isNotEmpty() || !scenarioEvts.value.isNullOrEmpty()
        }

    /**
     * Creates a new event item.
     * @param context the Android context.
     * @return the new event item.
     */
    fun createNewEvent(context: Context, event: Event? = null): Event = with(editionRepository.editedItemsBuilder) {
        if (event == null) createNewEvent(context)
        else createNewEventFrom(event)
    }

    fun startEventEdition(event: Event) = editionRepository.startEventEdition(event)

    /** Add or update an event. If the event id is unset, it will be added. If not, updated. */
    fun saveEventEdition() = editionRepository.upsertEditedEvent()

    /** Delete an event. */
    fun deleteEditedEvent() = editionRepository.deleteEditedEvent()

    /** Drop all changes made to the currently edited event. */
    fun dismissEditedEvent() = editionRepository.stopEventEdition()

    /** Update the priority of the events in the scenario. */
    fun updateEventsPriority(events: List<Event>) = editionRepository.updateEventsOrder(events)

    fun onEventCountReachedAddCopyClicked(context: Context) {
        billingRepository.startBillingActivity(context, ProModeAdvantage.Limitation.EVENT_COUNT_LIMIT)
    }
}
