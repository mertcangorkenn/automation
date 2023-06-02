 
package com.buzbuz.smartautoclicker.core.domain.model.condition

import android.graphics.Rect

import com.buzbuz.smartautoclicker.core.database.entity.ConditionEntity
import com.buzbuz.smartautoclicker.core.domain.model.Identifier

/** @return the entity equivalent of this condition. */
internal fun Condition.toEntity() = ConditionEntity(
    id = id.databaseId,
    eventId = eventId.databaseId,
    name = name,
    path = path!!,
    areaLeft = area.left,
    areaTop = area.top,
    areaRight = area.right,
    areaBottom = area.bottom,
    threshold = threshold,
    detectionType = detectionType,
    shouldBeDetected = shouldBeDetected,
)

/** @return the condition for this entity. */
internal fun ConditionEntity.toCondition(asDomain: Boolean = false) = Condition(
    id = Identifier(id = id, asDomain = asDomain),
    eventId = Identifier(id = eventId, asDomain = asDomain),
    name = name,
    path = path,
    area = Rect(areaLeft, areaTop, areaRight, areaBottom),
    threshold = threshold,
    detectionType = detectionType,
    shouldBeDetected = shouldBeDetected,
)