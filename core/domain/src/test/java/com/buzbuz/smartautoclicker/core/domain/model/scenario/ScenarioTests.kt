 
package com.buzbuz.smartautoclicker.core.domain.model.scenario

import android.os.Build

import androidx.test.ext.junit.runners.AndroidJUnit4

import com.buzbuz.smartautoclicker.core.database.entity.ScenarioWithEvents
import com.buzbuz.smartautoclicker.core.domain.utils.TestsData

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.annotation.Config

/** Tests the [Scenario] class. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class ScenarioTests {

    @Test
    fun toEntityNoCount() {
        assertEquals(
            TestsData.getNewScenarioEntity(),
            TestsData.getNewScenario().toEntity()
        )
    }

    @Test
    fun toEntityWithCount() {
        assertEquals(
            TestsData.getNewScenarioEntity(),
            TestsData.getNewScenario(eventCount = 18).toEntity()
        )
    }

    @Test
    fun toScenarioWithEvents() {
        val scenario = ScenarioWithEvents(
            TestsData.getNewScenarioEntity(),
            listOf(TestsData.getNewEventEntity(scenarioId = TestsData.SCENARIO_ID, priority = 0))
        ).toScenario()
        val expectedScenario = TestsData.getNewScenario(TestsData.SCENARIO_ID, TestsData.SCENARIO_NAME, eventCount = 1)

        assertEquals(scenario, expectedScenario)
    }

    @Test
    fun toScenarioComplete() {
        val scenario = ScenarioWithEvents(
            TestsData.getNewScenarioEntity(),
            listOf(TestsData.getNewEventEntity(scenarioId = TestsData.SCENARIO_ID, priority = 0))
        ).toScenario()
        val expectedScenario = TestsData.getNewScenario(
            TestsData.SCENARIO_ID,
            TestsData.SCENARIO_NAME,
            TestsData.SCENARIO_DETECTION_QUALITY,
            TestsData.SCENARIO_END_CONDITION_OPERATOR,
            eventCount = 1
        )

        assertEquals(scenario, expectedScenario)
    }
}