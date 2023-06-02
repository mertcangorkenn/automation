 
package com.buzbuz.smartautoclicker.core

import android.widget.TextView

import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Set the left compound drawable for this TextView.
 * All other compound drawable will be set to null.
 *
 * @param id the drawable to be set.
 * @param tint the tint color to apply to the drawable.
 */
fun TextView.setLeftCompoundDrawable(@DrawableRes id: Int, @ColorInt tint: Int = -1) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, id), null, null, null)
    if (tint != -1) {
        compoundDrawablesRelative[0].setTint(tint)
    }
}

/**
 * Set the right compound drawable for this TextView.
 * All other compound drawable will be set to null.
 *
 * @param id the drawable to be set.
 * @param tint the tint color to apply to the drawable.
 */
fun TextView.setRightCompoundDrawable(@DrawableRes id: Int?, @ColorInt tint: Int = -1) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, id?.let { ContextCompat.getDrawable(context, it) }, null)
    if (tint != -1) {
        compoundDrawablesRelative[0].setTint(tint)
    }
}

/**
 * Set the left compound drawable for this TextView.
 * All other compound drawable will be set to null.
 *
 * @param idLeft the drawable to be set to the left.
 * @param idRight: the drawable to be set to the right.
 * @param tint the tint color to apply to the left drawable.
 */
fun TextView.setLeftRightCompoundDrawables(@DrawableRes idLeft: Int, @DrawableRes idRight: Int, @ColorInt tint: Int = -1) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        ContextCompat.getDrawable(context, idLeft),
        null,
        ContextCompat.getDrawable(context, idRight),
        null)
    if (tint != -1) {
        compoundDrawablesRelative[0].setTint(tint)
    }
}