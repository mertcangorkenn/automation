 
package com.buzbuz.smartautoclicker.core.domain.model.scenario

import com.buzbuz.smartautoclicker.core.database.entity.ScenarioEntity
import com.buzbuz.smartautoclicker.core.database.entity.ScenarioWithEvents
import com.buzbuz.smartautoclicker.core.domain.model.Identifier

/** @return the entity equivalent of this scenario. */
internal fun Scenario.toEntity() = ScenarioEntity(
    id = id.databaseId,
    name = name,
    detectionQuality = detectionQuality,
    endConditionOperator = endConditionOperator,
    randomize = randomize,
)

/** @return the scenario for this entity. */
internal fun ScenarioEntity.toScenario(asDomain: Boolean = false) = Scenario(
    id = Identifier(id = id, asDomain = asDomain),
    name = name,
    detectionQuality = detectionQuality,
    endConditionOperator = endConditionOperator,
    randomize = randomize,
)

/** @return the scenario for this entity. */
internal fun ScenarioWithEvents.toScenario() = Scenario(
    id = Identifier(databaseId = scenario.id),
    name = scenario.name,
    detectionQuality = scenario.detectionQuality,
    endConditionOperator = scenario.endConditionOperator,
    randomize = scenario.randomize,
    eventCount = events.size,
)