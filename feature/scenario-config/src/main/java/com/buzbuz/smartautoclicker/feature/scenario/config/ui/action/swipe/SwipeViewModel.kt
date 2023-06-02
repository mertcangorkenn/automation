 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.swipe

import android.app.Application
import android.content.SharedPreferences
import android.graphics.Point

import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.core.domain.model.action.Action
import com.buzbuz.smartautoclicker.feature.scenario.config.domain.EditionRepository
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.getEventConfigPreferences
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.putSwipeDurationConfig

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull

class SwipeViewModel(application: Application) : AndroidViewModel(application) {

    /** Repository providing access to the edited items. */
    private val editionRepository = EditionRepository.getInstance(application)
    /** The action being configured by the user. */
    private val configuredSwipe = editionRepository.editionState.editedActionState
        .mapNotNull { action -> action.value }
        .filterIsInstance<Action.Swipe>()
    /** Event configuration shared preferences. */
    private val sharedPreferences: SharedPreferences = application.getEventConfigPreferences()

    /** The name of the swipe. */
    val name: Flow<String?> = configuredSwipe
        .map { it.name }
        .take(1)
    /** Tells if the action name is valid or not. */
    val nameError: Flow<Boolean> = configuredSwipe.map { it.name?.isEmpty() ?: true }

    /** The duration between the start and end of the swipe in milliseconds. */
    val swipeDuration: Flow<String?> = configuredSwipe
        .map { it.swipeDuration?.toString() }
        .take(1)
    /** Tells if the swipe duration value is valid or not. */
    val swipeDurationError: Flow<Boolean> = configuredSwipe.map { (it.swipeDuration ?: -1) <= 0 }

    /** The start and end positions of the swipe. */
    val positions: Flow<Pair<Point, Point>?> = configuredSwipe
        .map { swipe ->
            if (swipe.fromX != null && swipe.fromY != null && swipe.toX != null && swipe.toY != null) {
                Point(swipe.fromX!!, swipe.fromY!!) to Point(swipe.toX!!, swipe.toY!!)
            } else {
                null
            }
        }

    /** Tells if the configured swipe is valid and can be saved. */
    val isValidAction: Flow<Boolean> =  editionRepository.editionState.editedActionState
        .map { it.canBeSaved }

    /**
     * Set the name of the swipe.
     * @param name the new name.
     */
    fun setName(name: String) {
        editionRepository.editionState.getEditedAction<Action.Swipe>()?.let { swipe ->
            editionRepository.updateEditedAction(swipe.copy(name = "" + name))
        }
    }

    /**
     * Set the start and end positions of the swipe.
     * @param from the new start position.
     * @param to the new end position.
     */
    fun setPositions(from: Point, to: Point) {
        editionRepository.editionState.getEditedAction<Action.Swipe>()?.let { swipe ->
            editionRepository.updateEditedAction(swipe.copy(fromX = from.x, fromY = from.y, toX = to.x, toY = to.y))
        }
    }

    /**
     * Set the duration of the swipe.
     * @param durationMs the new duration in milliseconds.
     */
    fun setSwipeDuration(durationMs: Long?) {
        editionRepository.editionState.getEditedAction<Action.Swipe>()?.let { swipe ->
            editionRepository.updateEditedAction(swipe.copy(swipeDuration = durationMs))
        }
    }

    fun saveLastConfig() {
        editionRepository.editionState.getEditedAction<Action.Swipe>()?.let { swipe ->
            sharedPreferences.edit().putSwipeDurationConfig(swipe.swipeDuration ?: 0).apply()
        }
    }
}