 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.bindings

import android.util.TypedValue
import android.view.View

import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.ItemEventBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.utils.setIconTintColor

fun ItemEventBinding.bind(
    event: Event,
    canDrag: Boolean,
    itemClickedListener: (Event) -> Unit,
) {
    textName.text = event.name
    textConditionsCount.text = event.conditions.size.toString()
    textActionsCount.text = event.actions.size.toString()

    val typedValue = TypedValue()
    val actionColorAttr = if (!event.isComplete()) R.attr.colorError else R.attr.colorOnSurface
    root.context.theme.resolveAttribute(actionColorAttr, typedValue, true)
    textActionsCount.setTextColor(typedValue.data)
    imageAction.setIconTintColor(typedValue.data)

    root.setOnClickListener { itemClickedListener(event) }

    btnReorder.visibility = if (canDrag) View.VISIBLE else View.GONE
}