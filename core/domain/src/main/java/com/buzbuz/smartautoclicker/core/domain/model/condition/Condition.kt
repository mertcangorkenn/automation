 
package com.buzbuz.smartautoclicker.core.domain.model.condition

import android.graphics.Bitmap
import android.graphics.Rect

import com.buzbuz.smartautoclicker.core.domain.model.DetectionType
import com.buzbuz.smartautoclicker.core.domain.model.Identifier

/**
 * Condition for a Event.
 *
 * @param id the unique identifier for the condition.
 * @param eventId the identifier of the event for this condition.
 * @param name the name of the condition.
 * @param path the path to the bitmap that should be matched for detection.
 * @param area the area of the screen to detect.
 * @param threshold the accepted difference between the conditions and the screen content, in percent (0-100%).
 * @param detectionType the type of detection for this condition. Must be one of [DetectionType].
 * @param bitmap the bitmap for the condition. Not set when fetched from the repository.
 */
data class Condition(
    val id: Identifier,
    val eventId: Identifier,
    val name: String,
    val path: String? = null,
    val area: Rect,
    val threshold: Int,
    @DetectionType val detectionType: Int,
    val shouldBeDetected: Boolean,
    val bitmap: Bitmap? = null,
) {

    /** @return creates a deep copy of this condition. */
    fun deepCopy(): Condition = copy(
        path = "" + path,
        area = Rect(area),
    )

    /** Tells if this condition is complete and valid to be saved. */
    fun isComplete(): Boolean = name.isNotEmpty() && (path != null || bitmap != null)
}
