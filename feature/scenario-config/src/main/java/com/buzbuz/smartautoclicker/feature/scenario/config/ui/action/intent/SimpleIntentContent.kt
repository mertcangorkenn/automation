 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent

import android.content.Context
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.buzbuz.smartautoclicker.core.ui.bindings.setLabel
import com.buzbuz.smartautoclicker.core.ui.bindings.setOnTextChangedListener
import com.buzbuz.smartautoclicker.core.ui.bindings.setText
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.NavBarDialogContent
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.ContentIntentConfigSimpleBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.activities.ActivitySelectionDialog
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.bindings.bind
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.setError


import kotlinx.coroutines.launch

class SimpleIntentContent(appContext: Context) : NavBarDialogContent(appContext) {

    /** View model for the container dialog. */
    private val dialogViewModel: IntentViewModel by lazy {
        ViewModelProvider(dialogController).get(IntentViewModel::class.java)
    }

    /** View binding for all views in this content. */
    private lateinit var viewBinding: ContentIntentConfigSimpleBinding

    override fun onCreateView(container: ViewGroup): ViewGroup {
        viewBinding = ContentIntentConfigSimpleBinding.inflate(LayoutInflater.from(context)).apply {
            selectApplicationButton.setOnClickListener { showApplicationSelectionDialog() }
            selectedApplicationLayout.root.setOnClickListener { showApplicationSelectionDialog() }

            editNameLayout.apply {
                setLabel(R.string.input_field_label_name)
                setOnTextChangedListener { dialogViewModel.setName(it.toString()) }
                textField.filters = arrayOf<InputFilter>(
                    InputFilter.LengthFilter(context.resources.getInteger(R.integer.name_max_length))
                )
            }
            dialogController.hideSoftInputOnFocusLoss(editNameLayout.textField)
        }

        return viewBinding.root
    }

    override fun onViewCreated() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { dialogViewModel.name.collect(::updateClickName) }
                launch { dialogViewModel.nameError.collect(viewBinding.editNameLayout::setError)}
                launch { dialogViewModel.activityInfo.collect(::updateActivityInfo) }
            }
        }
    }

    private fun updateClickName(newName: String?) {
        viewBinding.editNameLayout.setText(newName)
    }

    private fun updateActivityInfo(activityInfo: ActivityDisplayInfo?) {
        viewBinding.apply {
            if (activityInfo == null) {
                selectedApplicationLayout.root.visibility = View.GONE
                selectApplicationButton.visibility = View.VISIBLE
            } else {
                selectedApplicationLayout.root.visibility = View.VISIBLE
                selectedApplicationLayout.bind(activityInfo)
                selectApplicationButton.visibility = View.GONE
            }
        }
    }

    private fun showApplicationSelectionDialog() {
        dialogController.showSubOverlay(
            overlayController = ActivitySelectionDialog(
                context = context,
                onApplicationSelected = { componentName ->
                    dialogViewModel.setActivitySelected(componentName)
                }
            ),
            false,
        )
    }
}