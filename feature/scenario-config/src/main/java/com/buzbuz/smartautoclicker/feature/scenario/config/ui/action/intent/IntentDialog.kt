 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent

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
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.NavBarDialogContent
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.NavBarDialogController
import com.buzbuz.smartautoclicker.feature.scenario.config.R

import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.coroutines.launch

class IntentDialog(
    context: Context,
    private val onConfirmClicked: () -> Unit,
    private val onDeleteClicked: () -> Unit,
    private val onDismissClicked: () -> Unit,
) : NavBarDialogController(context, R.style.ScenarioConfigTheme) {

    /** The view model for this dialog. */
    private val viewModel: IntentViewModel by lazy {
        ViewModelProvider(this).get(IntentViewModel::class.java)
    }

    override val navigationMenuId: Int = R.menu.menu_intent_config

    override fun onCreateView(): ViewGroup {
        return super.onCreateView().also {
            topBarBinding.apply {
                dialogTitle.setText(R.string.dialog_overlay_title_intent)
                setButtonVisibility(DialogNavigationButton.SAVE, View.VISIBLE)
                setButtonVisibility(DialogNavigationButton.DELETE, View.VISIBLE)
            }
        }
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        navBarView.selectedItemId =
            if (viewModel.isAdvanced()) R.id.page_advanced
            else R.id.page_simple

        super.onDialogCreated(dialog)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.isValidAction.collect(::updateSaveButton) }
            }
        }
    }

    override fun onCreateContent(navItemId: Int): NavBarDialogContent {
        return when (navItemId) {
            R.id.page_simple -> SimpleIntentContent(context.applicationContext)
            R.id.page_advanced -> AdvancedIntentContent(context.applicationContext)
            else -> throw IllegalArgumentException("Unknown menu id $navItemId")
        }
    }

    override fun onContentViewChanged(navItemId: Int) {
        when (navItemId) {
            R.id.page_simple -> viewModel.setIsAdvancedConfiguration(false)
            R.id.page_advanced -> viewModel.setIsAdvancedConfiguration(true)
        }
    }

    override fun onDialogButtonPressed(buttonType: DialogNavigationButton) {
        when (buttonType) {
            DialogNavigationButton.SAVE -> {
                viewModel.saveLastConfig()
                onConfirmClicked()
            }
            DialogNavigationButton.DELETE -> onDeleteClicked()
            DialogNavigationButton.DISMISS -> onDismissClicked()
            else -> {}
        }

        destroy()
    }

    private fun updateSaveButton(isValidCondition: Boolean) {
        topBarBinding.setButtonEnabledState(DialogNavigationButton.SAVE, isValidCondition)
    }
}