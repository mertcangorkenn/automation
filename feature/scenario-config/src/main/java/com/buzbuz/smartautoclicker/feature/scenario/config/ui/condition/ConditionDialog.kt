package com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton

import com.buzbuz.smartautoclicker.core.ui.bindings.DropdownItem
import com.buzbuz.smartautoclicker.core.ui.bindings.setItems
import com.buzbuz.smartautoclicker.core.ui.bindings.setLabel
import com.buzbuz.smartautoclicker.core.ui.bindings.setOnTextChangedListener
import com.buzbuz.smartautoclicker.core.ui.bindings.setButtonEnabledState
import com.buzbuz.smartautoclicker.core.ui.bindings.setSelectedItem
import com.buzbuz.smartautoclicker.core.ui.bindings.setText
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.OverlayDialogController
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.DialogConfigConditionBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.setError

import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ConditionDialog(
    context: Context,
    private val onConfirmClicked: () -> Unit,
    private val onDeleteClicked: () -> Unit,
    private val onDismissClicked: () -> Unit,
) : OverlayDialogController(context, R.style.ScenarioConfigTheme) {

    /** The view model for this dialog. */
    private val viewModel: ConditionViewModel by lazy {
        ViewModelProvider(this).get(ConditionViewModel::class.java)
    }

    /** ViewBinding containing the views for this dialog. */
    private lateinit var viewBinding: DialogConfigConditionBinding

    override fun onCreateView(): ViewGroup {
        viewBinding = DialogConfigConditionBinding.inflate(LayoutInflater.from(context)).apply {
            layoutTopBar.apply {
                dialogTitle.setText(R.string.dialog_overlay_title_condition_config)

                buttonDismiss.setOnClickListener {
                    onDismissClicked()
                    destroy()
                }
                buttonSave.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        onConfirmClicked()
                        destroy()
                    }
                }
                buttonDelete.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        onDeleteClicked()
                        destroy()
                    }
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

            conditionDetectionType.setItems(
                label = context.getString(R.string.dropdown_label_condition_detection_type),
                items = viewModel.detectionTypeItems,
                onItemSelected = viewModel::setDetectionType,
            )
        }

        return viewBinding.root
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.name.collect(::updateConditionName) }
                launch { viewModel.nameError.collect(viewBinding.editNameLayout::setError) }
                launch { viewModel.conditionBitmap.collect(::updateConditionBitmap) }
                launch { viewModel.detectionType.collect(::updateConditionType) }
                launch { viewModel.threshold.collect(::updateThreshold) }
                launch { viewModel.conditionCanBeSaved.collect(::updateSaveButton) }
            }
        }
    }

    private fun updateConditionName(newName: String?) {
        viewBinding.editNameLayout.setText(newName)
    }

    private fun updateConditionBitmap(newBitmap: Bitmap?) {
        if (newBitmap != null) {
            viewBinding.imageCondition.setImageBitmap(newBitmap)
        } else {
            viewBinding.imageCondition.setImageDrawable(
                ContextCompat.getDrawable(context, R.drawable.ic_cancel)?.apply {
                    setTint(Color.RED)
                }
            )
        }
    }

    private fun updateConditionType(newValue: DropdownItem) {
        viewBinding.conditionDetectionType.setSelectedItem(newValue)
    }

    private fun updateThreshold(newThreshold: Int) {
        viewBinding.apply {
        }
    }

    private fun updateSaveButton(isValidCondition: Boolean) {
        viewBinding.layoutTopBar.setButtonEnabledState(DialogNavigationButton.SAVE, isValidCondition)
    }
}