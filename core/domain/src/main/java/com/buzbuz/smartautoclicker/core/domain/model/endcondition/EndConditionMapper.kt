 
package com.buzbuz.smartautoclicker.core.domain.model.endcondition

import com.buzbuz.smartautoclicker.core.database.entity.EndConditionEntity
import com.buzbuz.smartautoclicker.core.database.entity.EndConditionWithEvent
import com.buzbuz.smartautoclicker.core.domain.model.Identifier

/** @return the entity equivalent of this end condition. */
internal fun EndCondition.toEntity(): EndConditionEntity {
    val evtId = eventId
    if (!scenarioId.isInDatabase() || evtId == null || !evtId.isInDatabase())
        throw IllegalStateException("Can't create entity, scenario or event is invalid")

    return EndConditionEntity(
        id = id.databaseId,
        scenarioId = scenarioId.databaseId,
        eventId = evtId.databaseId,
        executions = executions,
    )
}

/** @return the end condition for this entity. */
internal fun EndConditionWithEvent.toEndCondition(asDomain: Boolean = false) = EndCondition(
    id = Identifier(id = endCondition.id, asDomain = asDomain),
    scenarioId = Identifier(id = endCondition.scenarioId, asDomain = asDomain),
    eventId = Identifier(id = event.id, asDomain = asDomain),
    eventName = event.name,
    executions = endCondition.executions,
)