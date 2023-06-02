 
package com.buzbuz.smartautoclicker.core.ui.bindings

import android.text.Editable
import android.text.InputType
import android.view.inputmethod.EditorInfo

import androidx.annotation.StringRes

import com.buzbuz.smartautoclicker.core.ui.utils.OnAfterTextChangedListener
import com.buzbuz.smartautoclicker.core.ui.databinding.IncludeInputFieldTextBinding

fun IncludeInputFieldTextBinding.setLabel(@StringRes labelResId: Int) {
    root.setHint(labelResId)
}

fun IncludeInputFieldTextBinding.setText(text: String?, type: Int = InputType.TYPE_CLASS_TEXT) {
    textField.apply {
        inputType = type
        imeOptions = EditorInfo.IME_ACTION_DONE
        textField.setText(text)
    }
}

fun IncludeInputFieldTextBinding.setError(@StringRes messageId: Int, isError: Boolean) {
    root.error = if (isError) root.context.getString(messageId) else null
}

fun IncludeInputFieldTextBinding.setOnTextChangedListener(listener: (Editable) -> Unit) {
    textField.addTextChangedListener(OnAfterTextChangedListener(listener))
}