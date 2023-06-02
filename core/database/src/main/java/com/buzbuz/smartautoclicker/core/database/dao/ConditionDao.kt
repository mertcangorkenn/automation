 
package com.buzbuz.smartautoclicker.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

import com.buzbuz.smartautoclicker.core.database.entity.ConditionEntity

import kotlinx.coroutines.flow.Flow

/** Allows to access the conditions in the database. */
@Dao
abstract class ConditionDao {

    /**
     * Get all conditions from all events.
     *
     * @return the list containing all conditions.
     */
    @Query("SELECT * FROM condition_table")
    abstract fun getAllConditions(): Flow<List<ConditionEntity>>

    /**
     * Get the list of conditions for a given event.
     *
     * @param eventId the identifier of the event to get the conditions from.
     * @return the list of conditions for the event.
     */
    @Query("SELECT * FROM condition_table WHERE eventId=:eventId ORDER BY id")
    abstract suspend fun getConditions(eventId: Long): List<ConditionEntity>

    /**
     * Get the list of conditions path for a given event.
     *
     * @param eventId the identifier of the event to get the conditions path from.
     * @return the list of path for the event.
     */
    @Query("SELECT path FROM condition_table WHERE eventId=:eventId")
    abstract suspend fun getConditionsPath(eventId: Long): List<String>

    /**
     * Get the number of times this path is used in the condition table.
     *
     * @param path the value to be searched in the path column.
     * @return the number of conditions using this path.
     */
    @Query("SELECT COUNT(path) FROM condition_table WHERE path=:path")
    abstract suspend fun getValidPathCount(path: String): Int

    /**
     * Synchronize the conditions in the database.
     *
     * @param add the conditions to be added.
     * @param update the conditions to be updated.
     * @param delete the conditions to be deleted.
     */
    @Transaction
    open suspend fun syncConditions(
        add: List<ConditionEntity>,
        update: List<ConditionEntity>,
        delete: List<ConditionEntity>,
    ) {
        addConditions(add)
        updateConditions(update)
        deleteConditions(delete)
    }

    /**
     * Add conditions to the database.
     * @param conditions the conditions to be added.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addConditions(conditions: List<ConditionEntity>)

    /**
     * Update a condition in the database.
     * @param conditions the condition to be updated.
     */
    @Update
    protected abstract suspend fun updateConditions(conditions: List<ConditionEntity>)

    /**
     * Delete a list of conditions in the database.
     * @param conditions the conditions to be removed.
     */
    @Delete
    protected abstract suspend fun deleteConditions(conditions: List<ConditionEntity>)
}