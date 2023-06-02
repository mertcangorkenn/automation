 
package com.buzbuz.smartautoclicker.feature.scenario.config.data

import com.buzbuz.smartautoclicker.core.domain.model.action.Action
import com.buzbuz.smartautoclicker.core.domain.model.action.IntentExtra
import com.buzbuz.smartautoclicker.feature.scenario.config.data.base.ListEditor

class ActionsEditor(onListUpdated: (List<Action>) -> Unit): ListEditor<Action>(onListUpdated) {

    val intentExtraEditor = object : ListEditor<IntentExtra<out Any>>(::onEditedActionIntentExtraUpdated, true) {
        override fun areItemsTheSame(a: IntentExtra<out Any>, b: IntentExtra<out Any>): Boolean = a.id == b.id
        override fun isItemComplete(item: IntentExtra<out Any>): Boolean = item.isComplete()
    }

    override fun areItemsTheSame(a: Action, b: Action): Boolean = a.id == b.id
    override fun isItemComplete(item: Action): Boolean = item.isComplete()

    override fun startItemEdition(item: Action) {
        super.startItemEdition(item)
        if (item is Action.Intent) {
            intentExtraEditor.startEdition(item.extras ?: emptyList())
        }
    }

    override fun stopItemEdition() {
        intentExtraEditor.stopEdition()
        super.stopItemEdition()
    }

    private fun onEditedActionIntentExtraUpdated(extras: List<IntentExtra<out Any>>) {
        val action = editedItem.value
        if (action == null || action !is Action.Intent) return

        updateEditedItem(action.copy(extras = extras))
    }
}