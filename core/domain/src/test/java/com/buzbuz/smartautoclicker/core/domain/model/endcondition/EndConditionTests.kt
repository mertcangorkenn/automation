 
package com.buzbuz.smartautoclicker.core.domain.model.endcondition

import android.os.Build

import androidx.test.ext.junit.runners.AndroidJUnit4

import com.buzbuz.smartautoclicker.core.domain.utils.TestsData

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.annotation.Config

/** Tests the [EndCondition] class. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class EndConditionTests {

    @Test
    fun toEntity() {
        Assert.assertEquals(
            TestsData.getNewEndConditionEntity(),
            TestsData.getNewEndCondition().toEntity()
        )
    }

    @Test(expected = IllegalStateException::class)
    fun toEntityWithNoScenario() {
        TestsData.getNewEndCondition(scenarioId = 0L).toEntity()
    }

    @Test(expected = IllegalStateException::class)
    fun toEntityWithNoEvents() {
        TestsData.getNewEndCondition(eventId = 0L).toEntity()
    }

    @Test
    fun toEndCondition() {
        val defaultEvent = TestsData.getNewEventEntity(scenarioId = TestsData.SCENARIO_ID, priority = 0)

        Assert.assertEquals(
            TestsData.getNewEndCondition(),
            TestsData.getNewEndConditionWithEvent(event = defaultEvent).toEndCondition()
        )
    }
}