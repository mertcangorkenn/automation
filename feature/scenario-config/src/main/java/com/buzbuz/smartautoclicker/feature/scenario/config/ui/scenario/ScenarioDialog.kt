 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario

import android.content.Context
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton
import com.buzbuz.smartautoclicker.core.ui.bindings.setButtonEnabledState
import com.buzbuz.smartautoclicker.core.ui.bindings.setButtonVisibility
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.NavBarDialogController
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.NavBarDialogContent
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.NavigationRequest
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.config.ScenarioConfigContent
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.eventlist.EventListContent
import com.buzbuz.smartautoclicker.feature.scenario.debugging.ui.content.DebugConfigContent

import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.coroutines.launch

class ScenarioDialog(
    context: Context,
    private val onConfigSaved: () -> Unit,
    private val onConfigDiscarded: () -> Unit,
) : NavBarDialogController(context, R.style.ScenarioConfigTheme) {

    /** The view model for this dialog. */
    private val viewModel: ScenarioDialogViewModel by lazy {
        ViewModelProvider(this).get(ScenarioDialogViewModel::class.java)
    }

    override val navigationMenuId: Int = R.menu.menu_scenario_config

    override fun onCreateView(): ViewGroup {
        return super.onCreateView().also {
            topBarBinding.setButtonVisibility(DialogNavigationButton.SAVE, View.VISIBLE)
            topBarBinding.dialogTitle.setText(R.string.dialog_overlay_title_scenario_config)
        }
    }

    override fun onCreateContent(navItemId: Int): NavBarDialogContent = when (navItemId) {
        R.id.page_events -> EventListContent(context.applicationContext)
        else -> throw IllegalArgumentException("Unknown menu id $navItemId")
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        super.onDialogCreated(dialog)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.navItemsValidity.collect(::updateContentsValidity) }
                launch { viewModel.scenarioCanBeSaved.collect(::updateSaveButtonState) }
                launch { viewModel.subOverlayRequest.collect(::onNewSubOverlayRequest) }
            }
        }
    }

    override fun onDialogButtonPressed(buttonType: DialogNavigationButton) {
        when (buttonType) {
            DialogNavigationButton.SAVE -> onConfigSaved()
            DialogNavigationButton.DISMISS -> onConfigDiscarded()
            else -> { /* Nothing to do */ }
        }

        destroy()
    }

    private fun updateContentsValidity(itemsValidity: Map<Int, Boolean>) {
        itemsValidity.forEach { (itemId, isValid) ->
            setMissingInputBadge(itemId, !isValid)
        }
    }

    /** */
    private fun updateSaveButtonState(isEnabled: Boolean) {
        topBarBinding.setButtonEnabledState(DialogNavigationButton.SAVE, isEnabled)
    }

    private fun onNewSubOverlayRequest(request: NavigationRequest?) {
        if (request == null) return

        showSubOverlay(request.overlay, request.hideCurrent)
        viewModel.consumeRequest()
    }
}