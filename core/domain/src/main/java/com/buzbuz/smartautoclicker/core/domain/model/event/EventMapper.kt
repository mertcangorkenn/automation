 
package com.buzbuz.smartautoclicker.core.domain.model.event

import com.buzbuz.smartautoclicker.core.database.entity.CompleteEventEntity
import com.buzbuz.smartautoclicker.core.database.entity.EventEntity
import com.buzbuz.smartautoclicker.core.domain.model.Identifier
import com.buzbuz.smartautoclicker.core.domain.model.action.toAction
import com.buzbuz.smartautoclicker.core.domain.model.condition.toCondition

/** @return the entity equivalent of this event. */
internal fun Event.toEntity() = EventEntity(
    id = id.databaseId,
    scenarioId = scenarioId.databaseId,
    name = name,
    conditionOperator = conditionOperator,
    priority = priority,
    enabledOnStart = enabledOnStart,
)

/** @return the complete event for this entity. */
internal fun CompleteEventEntity.toEvent(asDomain: Boolean = false) = Event(
    id = Identifier(id = event.id, asDomain = asDomain),
    scenarioId = Identifier(id = event.scenarioId, asDomain = asDomain),
    name= event.name,
    conditionOperator = event.conditionOperator,
    priority = event.priority,
    enabledOnStart = event.enabledOnStart,
    actions = actions.sortedBy { it.action.priority }.map { it.toAction(asDomain) }.toMutableList(),
    conditions = conditions.map { it.toCondition(asDomain) }.toMutableList(),
)