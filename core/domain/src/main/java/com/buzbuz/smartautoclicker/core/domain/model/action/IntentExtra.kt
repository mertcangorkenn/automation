 
package com.buzbuz.smartautoclicker.core.domain.model.action

import android.content.Intent

import com.buzbuz.smartautoclicker.core.domain.model.Identifier

/**
 * Extras for a Intent action.
 *
 * @param T the type of the extra value. Must be one of the supported types.
 * @param id the unique identifier for the extra. Use 0 for creating a new extra. Default value is 0.
 * @param actionId the identifier of the intent action for this extra.
 * @param key the key of the extra.
 * @param value the value of the extra.
 */
data class IntentExtra<T>(
    val id: Identifier,
    val actionId: Identifier,
    val key: String?,
    val value: T?,
) {

    /** @return true if this extra is complete and can be transformed into its entity. */
    fun isComplete(): Boolean = key != null && value != null

    /**
     * Copy and change the type of the value contained in this IntentExtra.
     * @param V the new value type.
     * @param value the new value.
     */
    fun <V> changeType(value: V): IntentExtra<V> {
        if (value !is Boolean && value !is Byte && value !is Char && value !is Double && value !is Int &&
            value !is Float && value !is Short && value !is String) {
            throw IllegalArgumentException("Unsupported value type")
        }

        return IntentExtra(id = id, actionId = actionId, key = key, value = value)
    }
}

/**
 * Add the provided intent extra into the Android intent.
 * @param extra the extra to be added.
 */
fun Intent.putDomainExtra(extra: IntentExtra<out Any>): Intent {
    when (val value = extra.value) {
        is Byte -> putExtra(extra.key, value)
        is Boolean -> putExtra(extra.key, value)
        is Char -> putExtra(extra.key, value)
        is Double -> putExtra(extra.key, value)
        is Int -> putExtra(extra.key, value)
        is Float -> putExtra(extra.key, value)
        is Short -> putExtra(extra.key, value)
        is String -> putExtra(extra.key, value)
        else -> throw IllegalArgumentException("Unsupported value type")
    }
    return this
}