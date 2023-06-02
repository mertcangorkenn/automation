 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.config

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.buzbuz.smartautoclicker.core.domain.model.endcondition.EndCondition
import com.buzbuz.smartautoclicker.feature.scenario.config.R
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.ItemEndConditionCardBinding
import com.buzbuz.smartautoclicker.feature.scenario.config.databinding.ItemNewCopyCardBinding

/**
 * Adapter displaying a list of end conditions.
 *
 * @param addEndConditionClickedListener listener called when the user clicks on the add end condition item.
 * @param endConditionClickedListener listener called when the user clicks on an existing end condition.
 */
class EndConditionAdapter(
    private val addEndConditionClickedListener: () -> Unit,
    private val endConditionClickedListener: (EndCondition) -> Unit,
) : ListAdapter<EndConditionListItem, RecyclerView.ViewHolder>(EndConditionDiffUtilCallback) {

    private var recyclerView: RecyclerView? = null

    private val recyclerViewEnabledState: Boolean
        get() = recyclerView?.parent?.let { (it as View).isEnabled } ?: false

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is EndConditionListItem.AddEndConditionItem -> R.layout.item_new_copy_card
            is EndConditionListItem.EndConditionItem -> R.layout.item_end_condition_card
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_new_copy_card -> AddEndConditionViewHolder(
                viewBinding = ItemNewCopyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                addEndConditionClickedListener = { if (recyclerViewEnabledState) addEndConditionClickedListener() },
            )

            R.layout.item_end_condition_card -> EndConditionViewHolder(
                viewBinding = ItemEndConditionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                endConditionClickedListener = { if (recyclerViewEnabledState) endConditionClickedListener(it) },
            )

            else -> throw IllegalArgumentException("Unsupported view type !")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EndConditionViewHolder -> holder.onBind((getItem(position) as EndConditionListItem.EndConditionItem))
            else -> { /* Nothing to do */}
        }
    }
}

/** DiffUtil Callback comparing two EndConditionListItem when updating the [EndConditionAdapter] list. */
object EndConditionDiffUtilCallback: DiffUtil.ItemCallback<EndConditionListItem>() {
    override fun areItemsTheSame(oldItem: EndConditionListItem, newItem: EndConditionListItem): Boolean = when {
        oldItem is EndConditionListItem.AddEndConditionItem && newItem is EndConditionListItem.AddEndConditionItem -> true
        oldItem is EndConditionListItem.EndConditionItem && newItem is EndConditionListItem.EndConditionItem ->
            oldItem.endCondition.id == newItem.endCondition.id
        else -> false
    }

    override fun areContentsTheSame(oldItem: EndConditionListItem, newItem: EndConditionListItem): Boolean =
        oldItem == newItem
}

/** View holder for the add end condition item. */
class AddEndConditionViewHolder(
    viewBinding: ItemNewCopyCardBinding,
    addEndConditionClickedListener: () -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.newItem.setOnClickListener { addEndConditionClickedListener() }
        viewBinding.copyItem.visibility = View.GONE
    }
}

/** View holder for the end condition item. */
class EndConditionViewHolder(
    private val viewBinding: ItemEndConditionCardBinding,
    private val endConditionClickedListener: (EndCondition) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    fun onBind(item: EndConditionListItem.EndConditionItem) {
        viewBinding.apply {
            root.setOnClickListener { endConditionClickedListener(item.endCondition) }
            textEndConditionName.text = item.endCondition.eventName
            textEndConditionExecutions.text = itemView.context.getString(
                R.string.dialog_scenario_settings_end_condition_card_executions,
                item.endCondition.executions,
            )
        }
    }
}