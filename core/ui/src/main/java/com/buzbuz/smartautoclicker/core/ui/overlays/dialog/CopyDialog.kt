 
package com.buzbuz.smartautoclicker.core.ui.overlays.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.StyleRes
import androidx.appcompat.widget.SearchView

import com.buzbuz.smartautoclicker.core.ui.bindings.setEmptyText
import com.buzbuz.smartautoclicker.core.ui.databinding.DialogBaseCopyBinding

abstract class CopyDialog(
    context: Context,
    @StyleRes theme: Int,
) : OverlayDialogController(context, theme) {

    /** ViewBinding containing the views for this dialog. */
    protected lateinit var viewBinding: DialogBaseCopyBinding
    /** The resource id for the dialog title. */
    protected abstract val titleRes: Int
    /** The resource id for the text displayed when there is nothing to copy. */
    protected abstract val emptyRes: Int

    final override fun onCreateView(): ViewGroup {
        viewBinding = DialogBaseCopyBinding.inflate(LayoutInflater.from(context)).apply {
            layoutTopBar.apply {
                dialogTitle.setText(titleRes)
                buttonDismiss.setOnClickListener { destroy() }

                search.apply {
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?) = false
                        override fun onQueryTextChange(newText: String?): Boolean {
                            onSearchQueryChanged(newText)
                            return true
                        }
                    })
                    setOnSearchClickListener {
                        dialogTitle.visibility = View.GONE
                        buttonDismiss.visibility = View.GONE
                    }
                    setOnCloseListener {
                        dialogTitle.visibility = View.VISIBLE
                        buttonDismiss.visibility = View.VISIBLE
                        false
                    }
                }
            }

            layoutLoadableList.setEmptyText(emptyRes)
        }

        return viewBinding.root
    }

    abstract fun onSearchQueryChanged(newText: String?)
}
