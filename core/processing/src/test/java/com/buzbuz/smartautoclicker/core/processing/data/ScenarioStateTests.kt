 
package com.buzbuz.smartautoclicker.core.processing.data

import android.os.Build

import androidx.test.ext.junit.runners.AndroidJUnit4

import com.buzbuz.smartautoclicker.core.domain.model.action.Action
import com.buzbuz.smartautoclicker.core.processing.data.ScenarioState
import com.buzbuz.smartautoclicker.core.processing.utils.ProcessingData

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.annotation.Config

/** Test the [ScenarioState] class. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class ScenarioStateTests {

    @Test
    fun all_events_enabled_on_start() {
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = true),
            ProcessingData.newEvent(id = 2L, enableOnStart = true),
            ProcessingData.newEvent(id = 3L, enableOnStart = true),
        )

        val scenarioState = ScenarioState(eventList)

        Assert.assertEquals("Invalid enabled events count", eventList.size, scenarioState.getEnabledEvents().size)
    }

    @Test
    fun all_events_disabled_on_start() {
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            ProcessingData.newEvent(id = 2L, enableOnStart = false),
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList)

        Assert.assertEquals("Invalid enabled events count", 0, scenarioState.getEnabledEvents().size)
    }

    @Test
    fun mixed_events_enabled_disabled_on_start() {
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            ProcessingData.newEvent(id = 2L, enableOnStart = true),
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList)

        Assert.assertEquals("Invalid enabled events count", 1, scenarioState.getEnabledEvents().size)
    }

    @Test
    fun change_state_enabled_from_disabled() {
        val changingEvent = ProcessingData.newEvent(id = 2L, enableOnStart = false)
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            changingEvent,
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList).apply {
            changeEventState(changingEvent.id.databaseId, Action.ToggleEvent.ToggleType.ENABLE)
        }

        Assert.assertTrue("Event not enabled", scenarioState.getEnabledEvents().contains(changingEvent))
    }

    @Test
    fun change_state_enabled_from_enabled() {
        val changingEvent = ProcessingData.newEvent(id = 2L, enableOnStart = true)
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            changingEvent,
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList).apply {
            changeEventState(changingEvent.id.databaseId, Action.ToggleEvent.ToggleType.ENABLE)
        }

        Assert.assertTrue("Event not enabled", scenarioState.getEnabledEvents().contains(changingEvent))
    }

    @Test
    fun change_state_disabled_from_enabled() {
        val changingEvent = ProcessingData.newEvent(id = 2L, enableOnStart = true)
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            changingEvent,
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList).apply {
            changeEventState(changingEvent.id.databaseId, Action.ToggleEvent.ToggleType.DISABLE)
        }

        Assert.assertFalse("Event should not be enabled", scenarioState.getEnabledEvents().contains(changingEvent))
    }

    @Test
    fun change_state_disabled_from_disabled() {
        val changingEvent = ProcessingData.newEvent(id = 2L, enableOnStart = false)
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            changingEvent,
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList).apply {
            changeEventState(changingEvent.id.databaseId, Action.ToggleEvent.ToggleType.DISABLE)
        }

        Assert.assertFalse("Event should not be enabled", scenarioState.getEnabledEvents().contains(changingEvent))
    }

    @Test
    fun change_state_toggle_from_enabled() {
        val changingEvent = ProcessingData.newEvent(id = 2L, enableOnStart = true)
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            changingEvent,
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList).apply {
            changeEventState(changingEvent.id.databaseId, Action.ToggleEvent.ToggleType.TOGGLE)
        }

        Assert.assertFalse("Event should not be enabled", scenarioState.getEnabledEvents().contains(changingEvent))
    }

    @Test
    fun change_state_toggle_from_disabled() {
        val changingEvent = ProcessingData.newEvent(id = 2L, enableOnStart = false)
        val eventList = listOf(
            ProcessingData.newEvent(id = 1L, enableOnStart = false),
            changingEvent,
            ProcessingData.newEvent(id = 3L, enableOnStart = false),
        )

        val scenarioState = ScenarioState(eventList).apply {
            changeEventState(changingEvent.id.databaseId, Action.ToggleEvent.ToggleType.ENABLE)
        }

        Assert.assertTrue("Event not enabled", scenarioState.getEnabledEvents().contains(changingEvent))
    }
}