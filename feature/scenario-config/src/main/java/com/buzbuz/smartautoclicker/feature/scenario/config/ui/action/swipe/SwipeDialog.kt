 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.swipe

import android.content.Context
import android.graphics.Point
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
import com.buzbuz.smartautoclicker.core.ui.utils.MinMaxInputFilter
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.OverlayDialogController
import com.buzbuz.smartautoclicker.core.domain.model.action.GESTURE_DURATION_MAX_VALUE
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.DialogConfigActionSwipeBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.ClickSwipeSelectorMenu
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.CoordinatesSelector
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.setError
import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.coroutines.launch

class SwipeDialog(
    context: Context,
    private val onConfirmClicked: () -> Unit,
    private val onDeleteClicked: () -> Unit,
    private val onDismissClicked: () -> Unit,
) : OverlayDialogController(context, R.style.ScenarioConfigTheme) {

    /** The view model for this dialog. */
    private val viewModel: SwipeViewModel by lazy {
        ViewModelProvider(this).get(SwipeViewModel::class.java)
    }

    /** ViewBinding containing the views for this dialog. */
    private lateinit var viewBinding: DialogConfigActionSwipeBinding

    override fun onCreateView(): ViewGroup {
        viewBinding = DialogConfigActionSwipeBinding.inflate(LayoutInflater.from(context)).apply {
            layoutTopBar.apply {
                dialogTitle.setText(R.string.dialog_overlay_title_swipe)

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

            editSwipeDurationLayout.apply {
                textField.filters = arrayOf(MinMaxInputFilter(0, GESTURE_DURATION_MAX_VALUE.toInt()))
                setLabel(R.string.input_field_label_swipe_duration)
                setOnTextChangedListener {
                    viewModel.setSwipeDuration(if (it.isNotEmpty()) it.toString().toLong() else null)
                }
            }
            hideSoftInputOnFocusLoss(editSwipeDurationLayout.textField)

            onPositionSelectButton.setOnClickListener { showPositionSelector() }
        }

        return viewBinding.root
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.name.collect(::updateClickName) }
                launch { viewModel.nameError.collect(viewBinding.editNameLayout::setError)}
                launch { viewModel.swipeDuration.collect(::updateSwipeDuration) }
                launch { viewModel.swipeDurationError.collect(viewBinding.editSwipeDurationLayout::setError)}
                launch { viewModel.positions.collect(::updateSwipePositionsButtonText) }
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

    private fun updateSwipeDuration(newDuration: String?) {
        viewBinding.editSwipeDurationLayout.setText(newDuration, InputType.TYPE_CLASS_NUMBER)
    }

    private fun updateSwipePositionsButtonText(positions: Pair<Point, Point>?) {
        if (positions == null) {
            viewBinding.onPositionSelectButton.setText(R.string.button_text_swipe_positions_select)
            return
        }

        viewBinding.onPositionSelectButton.text = context.getString(
            R.string.item_desc_swipe_positions,
            positions.first.x,
            positions.first.y,
            positions.second.x,
            positions.second.y,
        )
    }

    private fun updateSaveButton(isValidCondition: Boolean) {
        viewBinding.layoutTopBar.setButtonEnabledState(com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.SAVE, isValidCondition)
    }

    private fun showPositionSelector() {
        showSubOverlay(
            overlayController = ClickSwipeSelectorMenu(
                context = context,
                selector = CoordinatesSelector.Two(),
                onCoordinatesSelected = { selector ->
                    (selector as CoordinatesSelector.Two).let {
                        viewModel.setPositions(it.coordinates1!!, it.coordinates2!!)
                    }
                },
            ),
            hideCurrent = true,
        )
    }
}