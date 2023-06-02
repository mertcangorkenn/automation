 
package com.buzbuz.smartautoclicker.feature.scenario.config.domain

data class EditedElementState<EditedType>(
    val value: EditedType?,
    val hasChanged: Boolean,
    val canBeSaved: Boolean,
)