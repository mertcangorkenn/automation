 
package com.buzbuz.smartautoclicker.core.ui.overlays.dialog

import android.app.Application
import androidx.lifecycle.AndroidViewModel

import kotlinx.coroutines.flow.MutableStateFlow

abstract class CopyViewModel<I>(application: Application) : AndroidViewModel(application) {

    /** The currently searched action name. Null if no is. */
    protected val searchQuery = MutableStateFlow<String?>(null)
    /** The list of items from  the configured container. They are not all available yet in the database. */
    protected val itemsFromCurrentContainer = MutableStateFlow<List<I>?>(null)

    /**
     * Set the current container items.
     * @param items the items.
     */
    fun setItemsFromContainer(items: List<I>) {
        itemsFromCurrentContainer.value = items
    }

    /**
     * Update the action search query.
     * @param query the new query.
     */
    fun updateSearchQuery(query: String?) {
        searchQuery.value = query
    }
}