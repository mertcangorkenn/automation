 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.buzbuz.smartautoclicker.core.ui.overlays.OverlayController

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    /** Backing field for [subOverlayRequest]. */
    private val _subOverlayRequest = MutableStateFlow<NavigationRequest?>(null)
    /** The subOverlay currently requested by the user. Changed with [requestSubOverlay], consumed with [consumeRequest] */
    val subOverlayRequest: Flow<NavigationRequest?> = _subOverlayRequest

    /**
     * Request a new sub overlay.
     * @param request the type of overlay requested.
     */
    fun requestSubOverlay(request: NavigationRequest) {
        viewModelScope.launch {
            _subOverlayRequest.emit(request)
        }
    }

    /** Consume the current sub overlay request and replace it by null. */
    fun consumeRequest() {
        viewModelScope.launch {
            _subOverlayRequest.emit(null)
        }
    }
}

class NavigationRequest(
    val overlay: OverlayController,
    val hideCurrent: Boolean = false,
)