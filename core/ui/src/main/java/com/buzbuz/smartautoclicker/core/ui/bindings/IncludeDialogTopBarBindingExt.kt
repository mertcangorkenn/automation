 
package com.buzbuz.smartautoclicker.core.ui.bindings

import com.buzbuz.smartautoclicker.core.ui.databinding.IncludeDialogNavigationTopBarBinding

fun IncludeDialogNavigationTopBarBinding.setButtonEnabledState(buttonType: com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton, enabled: Boolean) {
    when (buttonType) {
        com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.SAVE -> buttonSave.isEnabled = enabled
        com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.DISMISS -> buttonDismiss.isEnabled = enabled
        com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.DELETE -> buttonDelete.isEnabled = enabled
    }
}

fun IncludeDialogNavigationTopBarBinding.setButtonVisibility(buttonType: com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton, visibility: Int) {
    when (buttonType) {
        com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.SAVE -> buttonSave.visibility = visibility
        com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.DISMISS -> buttonDismiss.visibility = visibility
        com.buzbuz.smartautoclicker.core.ui.bindings.DialogNavigationButton.DELETE -> buttonDelete.visibility = visibility
    }
}

enum class DialogNavigationButton {
    DISMISS,
    DELETE,
    SAVE,
}