 
package com.buzbuz.smartautoclicker.feature.scenario.config.domain

import com.buzbuz.smartautoclicker.core.domain.model.endcondition.EndCondition
import com.buzbuz.smartautoclicker.core.domain.model.event.Event
import com.buzbuz.smartautoclicker.core.domain.model.scenario.Scenario

data class ScenarioEditionState(
    val scenario: Scenario,
    val events: List<Event>,
    val endConditions: List<EndCondition>,
)