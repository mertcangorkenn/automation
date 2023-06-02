 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.bindings

import android.graphics.Bitmap
import android.graphics.Color

import androidx.core.content.ContextCompat

import com.buzbuz.smartautoclicker.core.domain.model.condition.Condition
import com.buzbuz.smartautoclicker.core.domain.model.EXACT
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.ItemConditionBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.setIconTint

import kotlinx.coroutines.Job

/**
 * Bind the [ItemConditionBinding] to a condition.
 */
fun ItemConditionBinding.bind(
    condition: Condition,
    bitmapProvider: (Condition, onBitmapLoaded: (Bitmap?) -> Unit) -> Job?,
    conditionClickedListener: (Condition) -> Unit
): Job? {
    root.setOnClickListener { conditionClickedListener.invoke(condition) }

    conditionName.text = condition.name

    conditionShouldBeDetected.apply {
        if (condition.shouldBeDetected) {
            setImageResource(R.drawable.ic_confirm)
            setIconTint(R.color.overlayMenuButtons)
        } else {
            setImageResource(R.drawable.ic_cancel)
            setIconTint(R.color.overlayMenuButtons)
        }
    }

    conditionDetectionType.apply {
        setImageResource(
            if (condition.detectionType == EXACT) R.drawable.ic_detect_exact else R.drawable.ic_detect_whole_screen
        )
        setIconTint(R.color.overlayMenuButtons)
    }


    conditionThreshold.text = root.context.getString(
        R.string.message_condition_threshold,
        condition.threshold
    )

    return bitmapProvider.invoke(condition) { bitmap ->
        if (bitmap != null) {
            conditionImage.setImageBitmap(bitmap)
        } else {
            conditionImage.setImageDrawable(
                ContextCompat.getDrawable(root.context, R.drawable.ic_cancel)?.apply {
                    setTint(Color.RED)
                }
            )
        }
    }
}