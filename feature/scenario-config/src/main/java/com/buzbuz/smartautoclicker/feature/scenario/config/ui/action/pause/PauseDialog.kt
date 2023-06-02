 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.pause

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.buzbuz.smartautoclicker.core.ui.bindings.setButtonEnabledState
import com.buzbuz.smartautoclicker.core.ui.bindings.setLabel
import com.buzbuz.smartautoclicker.core.ui.bindings.setOnTextChangedListener
import com.buzbuz.smartautoclicker.core.ui.bindings.setText
import com.buzbuz.smartautoclicker.core.ui.utils.DurationInputFilter
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.OverlayDialogController
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.DialogConfigActionPauseBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.setError

import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.coroutines.launch

class PauseDialog(
    context: Context,
    private val onConfirmClicked: () -> Unit,
    private val onDeleteClicked: () -> Unit,
    private val onDismissClicked: () -> Unit,
) : OverlayDialogController(context, R.style.ScenarioConfigTheme) {

    /** The view model for this dialog. */
    private val viewModel: PauseViewModel by lazy {
        ViewModelProvider(this).get(PauseViewModel::class.java)
    }

    /** ViewBinding containing the views for this dialog. */
    private lateinit var viewBinding: DialogConfigActionPauseBinding

    override fun onCreateView(): ViewGroup {
        viewBinding = DialogConfigActionPauseBinding.inflate(LayoutInflater.from(context)).apply {
            layoutTopBar.apply {
                dialogTitle.setText(R.string.dialog_overlay_title_pause)

                buttonDismiss.setOnClickListener {
                    onDismissClicked()
                    destroy()
                }
                buttonSave.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { onSaveButtonClicked() }
                }
                buttonDelete.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { onDeleteButtonClicked() }
                }
            }

            editNameLayout.apply {
                setLabel(R.string.input_field_label_name)
                setOnTextChangedListener { viewModel.setName(it.toString()) }
                textField.filters = arrayOf<InputFilter>(
                    InputFilter.LengthFilter(context.resources.getInteger(R.integer.name_max_length))
                )
            }
            hideSoftInputOnFocusLoss(editNameLayout.textField)

            editPauseDurationLayout.apply {
                textField.filters = arrayOf(DurationInputFilter())
                setLabel(R.string.input_field_label_pause_duration)
                setOnTextChangedListener {
                    viewModel.setPauseDuration(if (it.isNotEmpty()) it.toString().toLong() else null)
                }
            }
            hideSoftInputOnFocusLoss(editPauseDurationLayout.textField)
        }

        return viewBinding.root
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.name.collect(::updateClickName) }
                launch { viewModel.nameError.collect(viewBinding.editNameLayout::setError)}
                launch { viewModel.pauseDuration.collect(::updatePauseDuration) }
                launch { viewModel.pauseDurationError.collect(viewBinding.editPauseDurationLayout::setError)}
                launch { viewModel.isValidAction.collect(::updateSaveButton) }
            }
        }
    }

    private fun onSaveButtonClicked() {
        viewModel.saveLastConfig()
        onConfirmClicked()
        destroy()
    }

    private fun onDeleteButtonClicked() {
        onDeleteClicked()
        destroy()
    }

    private fun updateClickName(newName: String?) {
        viewBinding.editNameLayout.setText(newName)
    }

    private fun updatePauseDuration(newDuration: String?) {
        viewBinding.editPauseDurationLayout.setText(newDuration, InputType.TYPE_CLASS_NUMBER)
    }

    private fun updateSaveButton(isValidCondition: Boolean) {
        viewBinding.layoutTopBar.setButtonEnabledState(com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.SAVE, isValidCondition)
    }
}