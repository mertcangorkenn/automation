 
package com.buzbuz.smartautoclicker.core.domain.model.condition

import android.os.Build

import androidx.test.ext.junit.runners.AndroidJUnit4

import com.buzbuz.smartautoclicker.core.domain.utils.TestsData

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.annotation.Config

/** Tests the [Condition] class. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class ConditionTests {

    @Test
    fun toEntity() {
        assertEquals(
            TestsData.getNewConditionEntity(eventId = TestsData.EVENT_ID),
            TestsData.getNewCondition(eventId = TestsData.EVENT_ID).toEntity()
        )
    }

    @Test
    fun toDomain() {
        assertEquals(
            TestsData.getNewCondition(eventId = TestsData.EVENT_ID),
            TestsData.getNewConditionEntity(eventId = TestsData.EVENT_ID).toCondition()
        )
    }

    @Test
    fun deepCopy() {
        val condition = TestsData.getNewCondition(eventId = TestsData.EVENT_ID)
        assertEquals(condition, condition.deepCopy())
    }
}