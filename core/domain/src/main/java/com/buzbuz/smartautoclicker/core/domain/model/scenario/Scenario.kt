 
package com.buzbuz.smartautoclicker.core.domain.model.scenario

import com.buzbuz.smartautoclicker.core.database.entity.EndConditionEntity
import com.buzbuz.smartautoclicker.core.domain.model.ConditionOperator
import com.buzbuz.smartautoclicker.core.domain.model.Identifier

/**
 * Scenario of events.
 *
 * @param id the unique identifier for the scenario.
 * @param name the name of the scenario.
 * @param detectionQuality the quality of the detection algorithm. Lower value means faster detection but poorer
 *                         quality, while higher values means better and slower detection.
 * @param endConditionOperator the operator to apply to all [EndConditionEntity] related to this scenario. Can be any
 *                             value of [com.buzbuz.smartautoclicker.domain.ConditionOperator].
 * @param randomize tells if the actions values should be randomized a bit.
 * @param eventCount the number of events in this scenario. Default value is 0.
 */
data class Scenario(
    val id: Identifier,
    val name: String,
    val detectionQuality: Int,
    @ConditionOperator val endConditionOperator: Int,
    val randomize: Boolean = false,
    val eventCount: Int = 0,
)

/** The maximum detection quality for the algorithm. */
const val DETECTION_QUALITY_MAX = 1200
/** The minimum detection quality for the algorithm. */
const val DETECTION_QUALITY_MIN = 400