 
package com.buzbuz.smartautoclicker.feature.scenario.debugging.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.buzbuz.smartautoclicker.core.ui.overlays.dialog.OverlayDialogController
import com.buzbuz.smartautoclicker.feature.scenario.debugging.R
import com.buzbuz.smartautoclicker.feature.scenario.debugging.databinding.DialogDebugReportConditionBinding

import com.google.android.material.bottomsheet.BottomSheetDialog

/** Dialog displaying the full debug report for a condition. */
class DebugReportConditionDialog(
    context: Context,
    private val conditionReport: ConditionReport,
): OverlayDialogController(context, R.style.AppTheme) {

    /** ViewBinding containing the views for this dialog. */
    private lateinit var viewBinding: DialogDebugReportConditionBinding

    override fun onCreateView(): ViewGroup {
        viewBinding = DialogDebugReportConditionBinding.inflate(LayoutInflater.from(context)).apply {
            layoutTopBar.apply {
                dialogTitle.text = conditionReport.condition.name
                buttonSave.visibility = View.GONE
                buttonDismiss.setOnClickListener { destroy() }
            }
        }

        return viewBinding.root
    }

    override fun onDialogCreated(dialog: BottomSheetDialog) {
        viewBinding.apply {
            rootTriggerCount.apply {
                setValues(
                    R.string.section_title_report_condition_detected_count,
                    conditionReport.matchCount,
                    R.string.section_title_report_condition_processing_count,
                    conditionReport.processingCount,
                )
            }

            rootProcessingTiming.setValues(
                R.string.section_title_report_timing_title,
                conditionReport.minProcessingDuration,
                conditionReport.avgProcessingDuration,
                conditionReport.maxProcessingDuration,
            )

            rootConfidenceRate.setValues(
                R.string.section_title_report_confidence_title,
                conditionReport.minConfidence,
                conditionReport.avgConfidence,
                conditionReport.maxConfidence,
            )
        }
    }
}