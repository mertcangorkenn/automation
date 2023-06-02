 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.event

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup

import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.buzbuz.smartautoclicker.core.ui.bindings.setButtonEnabledState
import com.buzbuz.smartautoclicker.core.ui.bindings.setButtonVisibility
import com.buzbuz.smartautoclicker.core.display.DisplayMetrics
import com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.NavBarDialogContent
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.NavBarDialogController
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.NavigationRequest
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.actions.ActionsContent
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.conditions.ConditionsContent
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.config.EventConfigContent

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import kotlinx.coroutines.launch

class EventDialog(
    context: Context,
    private val onConfigComplete: () -> Unit,
    private val onDelete: () -> Unit,
    private val onDismiss: () -> Unit,
): NavBarDialogController(context, R.style.ScenarioConfigTheme) {

    /** View model for this dialog. */
    private val viewModel: EventDialogViewModel by lazy {
        ViewModelProvider(this).get(EventDialogViewModel::class.java)
    }

    override val navigationMenuId: Int = R.menu.menu_event_config

    override fun onCreateView(): ViewGroup {
        return super.onCreateView().also {
            topBarBinding.apply {
                setButtonVisibility(DialogNavigationButton.SAVE, View.VISIBLE)
                setButtonVisibility(DialogNavigationButton.DELETE, View.VISIBLE)
                dialogTitle.setText(R.string.dialog_overlay_title_event_config)
            }
        }
    }

    override fun onCreateContent(navItemId: Int): NavBarDialogContent {
        return when (navItemId) {
            R.id.page_conditions -> ConditionsContent(context.applicationContext)
            R.id.page_actions -> ActionsContent(context.applicationContext)
            else -> throw IllegalArgumentException("Unknown menu id $navItemId")
        }
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        super.onDialogCreated(dialog)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.navItemsValidity.collect(::updateContentsValidity) }
                launch { viewModel.eventCanBeSaved.collect(::updateSaveButton) }
                launch { viewModel.subOverlayRequest.collect(::onNewSubOverlayRequest) }
            }
        }
    }

    override fun onDialogButtonPressed(buttonType: DialogNavigationButton) {
        when (buttonType) {
            DialogNavigationButton.SAVE -> onConfigComplete()
            DialogNavigationButton.DELETE -> {
                onDeleteButtonPressed()
                return
            }
            DialogNavigationButton.DISMISS -> onDismiss()
            else -> {}
        }

        destroy()
    }

    private fun updateContentsValidity(itemsValidity: Map<Int, Boolean>) {
        itemsValidity.forEach { (itemId, isValid) ->
            setMissingInputBadge(itemId, !isValid)
        }
    }

    private fun updateSaveButton(enabled: Boolean) {
        topBarBinding.setButtonEnabledState(DialogNavigationButton.SAVE, enabled)
    }

    private fun onNewSubOverlayRequest(request: NavigationRequest?) {
        if (request == null) return

        showSubOverlay(request.overlay, request.hideCurrent)
        viewModel.consumeRequest()
    }

    /**
     * Called when the delete button is pressed.
     * It will display the relation warnings if needed, or delete the event immediately and close the dialog.
     */
    private fun onDeleteButtonPressed() {
        if (viewModel.isEventHaveRelatedEndConditions()) {
            showAssociatedEndConditionsWarning()
        } else if (viewModel.isEventHaveRelatedActions()) {
            showAssociatedActionsWarning()
        } else {
            onDelete()
            destroy()
        }
    }

    /**
     * Show the end condition relation warning dialog.
     * Once confirmed, it will show the action relation warning, or delete the event immediately and close the dialog.
     */
    private fun showAssociatedEndConditionsWarning() {
        showMessageDialog(R.string.dialog_overlay_title_warning, R.string.message_event_delete_associated_end_condition) {
            if (viewModel.isEventHaveRelatedActions()) {
                showAssociatedActionsWarning()
            } else {
                onDelete()
                destroy()
            }
        }
    }

    /**
     * Show the action relation warning dialog.
     * Once confirmed, it will delete the event and close the dialog.
     */
    private fun showAssociatedActionsWarning() {
        showMessageDialog(R.string.dialog_overlay_title_warning, R.string.message_event_delete_associated_action) {
            onDelete()
            destroy()
        }
    }

    /** Show a message dialog. */
    private fun showMessageDialog(@StringRes title: Int, @StringRes message: Int, onOkPressed: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                onOkPressed()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .apply {
                window?.setType(DisplayMetrics.TYPE_COMPAT_OVERLAY)
            }
            .show()
    }
}