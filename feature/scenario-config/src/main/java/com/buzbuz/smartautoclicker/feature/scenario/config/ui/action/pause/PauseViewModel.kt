 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.pause

import android.app.Application
import android.content.SharedPreferences

import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.core.domain.model.action.Action
import com.buzbuz.smartautoclicker.feature.scenario.config.domain.EditionRepository
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.getEventConfigPreferences
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.putPauseDurationConfig

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull

class PauseViewModel(application: Application) : AndroidViewModel(application) {

    /** Repository providing access to the edited items. */
    private val editionRepository = EditionRepository.getInstance(application)
    /** The action being configured by the user. */
    private val configuredPause = editionRepository.editionState.editedActionState
        .mapNotNull { action -> action.value }
        .filterIsInstance<Action.Pause>()
    /** Event configuration shared preferences. */
    private val sharedPreferences: SharedPreferences = application.getEventConfigPreferences()

    /** The name of the pause. */
    val name: Flow<String?> = configuredPause
        .map { it.name }
        .take(1)
    /** Tells if the action name is valid or not. */
    val nameError: Flow<Boolean> = configuredPause.map { it.name?.isEmpty() ?: true }

    /** The duration of the pause in milliseconds. */
    val pauseDuration: Flow<String?> = configuredPause
        .map { it.pauseDuration?.toString() }
        .take(1)
    /** Tells if the pause duration value is valid or not. */
    val pauseDurationError: Flow<Boolean> = configuredPause.map { (it.pauseDuration ?: -1) <= 0 }

    /** Tells if the configured pause is valid and can be saved. */
    val isValidAction: Flow<Boolean> = editionRepository.editionState.editedActionState
        .map { it.canBeSaved }

    /**
     * Set the name of the pause.
     * @param name the new name.
     */
    fun setName(name: String) {
        editionRepository.editionState.getEditedAction<Action.Pause>()?.let { pause ->
            editionRepository.updateEditedAction(pause.copy(name = "" + name))
        }
    }

    /**
     * Set the duration of the pause.
     * @param durationMs the new duration in milliseconds.
     */
    fun setPauseDuration(durationMs: Long?) {
        editionRepository.editionState.getEditedAction<Action.Pause>()?.let { pause ->
            editionRepository.updateEditedAction(pause.copy(pauseDuration = durationMs))
        }
    }

    fun saveLastConfig() {
        editionRepository.editionState.getEditedAction<Action.Pause>()?.let { pause ->
            sharedPreferences.edit().putPauseDurationConfig(pause.pauseDuration ?: 0).apply()
        }
    }
}