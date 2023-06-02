 
package com.buzbuz.smartautoclicker.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

import com.buzbuz.smartautoclicker.core.database.entity.CompleteEventEntity
import com.buzbuz.smartautoclicker.core.database.entity.EventEntity

import kotlinx.coroutines.flow.Flow

/** Allows to access and edit the events in the database. */
@Dao
abstract class EventDao {

    /**
     * Get all events from all scenarios.
     *
     * @return the list containing all events.
     */
    @Transaction
    @Query("SELECT * FROM event_table ORDER BY name")
    abstract fun getAllEvents(): Flow<List<CompleteEventEntity>>

    /**
     * Get the list of events for a scenario ordered by priority.
     *
     * @param scenarioId the identifier of the scenario to get the events from.
     * @return the flow on the list of events.
     */
    @Query("SELECT * FROM event_table WHERE scenario_id=:scenarioId ORDER BY priority")
    abstract fun getEventsFlow(scenarioId: Long): Flow<List<EventEntity>>

    /**
     * Get the list of events for a scenario ordered by priority.
     *
     * @param scenarioId the identifier of the scenario to get the events from.
     * @return the flow on the list of events.
     */
    @Query("SELECT * FROM event_table WHERE scenario_id=:scenarioId ORDER BY priority")
    abstract suspend fun getEvents(scenarioId: Long): List<EventEntity>

    /**
     * Get the list of complete events for a scenario ordered by priority.
     *
     * @param scenarioId the identifier of the scenario to get the events from.
     * @return the flow on the list of complete events.
     */
    @Transaction
    @Query("SELECT * FROM event_table WHERE scenario_id=:scenarioId ORDER BY priority")
    abstract fun getCompleteEventsFlow(scenarioId: Long): Flow<List<CompleteEventEntity>>

    /**
     * Get the list of complete events for a scenario ordered by priority.
     *
     * @param scenarioId the identifier of the scenario to get the events from.
     * @return the flow on the list of complete events.
     */
    @Transaction
    @Query("SELECT * FROM event_table WHERE scenario_id=:scenarioId ORDER BY priority")
    abstract suspend fun getCompleteEvents(scenarioId: Long): List<CompleteEventEntity>

    /**
     * Get the list of event identifier for a given scenario.
     *
     * @param scenarioId the identifier of the scenario to get the events from.
     * @return the flow on the list of events id.
     */
    @Transaction
    @Query("SELECT id FROM event_table WHERE scenario_id=:scenarioId")
    abstract suspend fun getEventsIds(scenarioId: Long): List<Long>

    /**
     * Get the list of events for a scenario ordered by priority.
     *
     * @param scenarioId the identifier of the scenario to get the events from.
     * @return the flow on the list of events.
     */
    @Transaction
    @Query("SELECT * FROM event_table WHERE scenario_id=:scenarioId AND id=:eventId")
    abstract fun getEvent(scenarioId: Long, eventId: Long): Flow<CompleteEventEntity>

    /**
     * Add an event to the database.
     * @param event the event to be added.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addEvent(event: EventEntity): Long

    /**
     * Update an event in the database.
     * @param event the event to be updated.
     */
    @Update
    abstract suspend fun updateEvent(event: EventEntity)

    /**
     * Delete an event list from the database.
     * Actions and conditions of this event will be deleted as well due to the CASCADE action on event deletion.
     *
     * @param events the events to be deleted
     */
    @Delete
    abstract suspend fun deleteEvents(events: List<EventEntity>)
}