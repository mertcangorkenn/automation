 
package com.buzbuz.smartautoclicker.feature.scenario.debugging.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.buzbuz.smartautoclicker.core.ui.bindings.updateState
import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.OverlayDialogController
import com.buzbuz.smartautoclicker.feature.scenario.debugging.R
import com.buzbuz.smartautoclicker.feature.scenario.debugging.databinding.DialogDebugReportBinding
import com.buzbuz.smartautoclicker.core.ui.databinding.IncludeLoadableListBinding

import com.google.android.material.bottomsheet.BottomSheetDialog

import kotlinx.coroutines.launch

/** Displays the content of the current debug report. */
class DebugReportDialog(context: Context): OverlayDialogController(context, R.style.AppTheme) {

    /** View model for this dialog. */
    private val viewModel: DebugReportModel by lazy { ViewModelProvider(this).get(DebugReportModel::class.java) }

    /** ViewBinding containing the views for this dialog. */
    private lateinit var viewBinding: DialogDebugReportBinding
    /** View binding for all views in this content. */
    private lateinit var listBinding: IncludeLoadableListBinding

    /** Adapter for the report */
    private val reportAdapter = DebugReportAdapter(
        bitmapProvider = viewModel::getConditionBitmap,
        onConditionClicked = ::showConditionReportDialog,
    )

    override fun onCreateView(): ViewGroup {
        viewBinding = DialogDebugReportBinding.inflate(LayoutInflater.from(context)).apply {
            layoutTopBar.apply {
                dialogTitle.setText(R.string.dialog_overlay_title_debug_report)
                buttonSave.visibility = View.GONE
                buttonDismiss.setOnClickListener { destroy() }
            }
        }

        listBinding = viewBinding.layoutList
        return viewBinding.root
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        listBinding.list.adapter = reportAdapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reportItems.collect(::updateReport)
            }
        }
    }

    private fun updateReport(reportItems: List<DebugReportItem>) {
        listBinding.updateState(reportItems)
        reportAdapter.submitList(reportItems)
    }

    private fun showConditionReportDialog(conditionReport: ConditionReport) {
        showSubOverlay(DebugReportConditionDialog(context, conditionReport))
    }
}