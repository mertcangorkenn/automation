 
package com.buzbuz.smartautoclicker.core.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

import com.buzbuz.smartautoclicker.core.database.entity.EndConditionEntity
import com.buzbuz.smartautoclicker.core.database.entity.EndConditionWithEvent

/** Allows to access and edit the end conditions in the database. */
@Dao
abstract class EndConditionDao {

    /**
     * Get all end conditions for a given scenario.
     *
     * @param scenarioId the unique identifier of the scenario.
     */
    @Transaction
    @Query("SELECT * FROM end_condition_table WHERE scenario_id=:scenarioId")
    abstract suspend fun getEndConditions(scenarioId: Long): List<EndConditionEntity>

    /**
     * Get all end conditions and their associated event for a given scenario.
     *
     * @param scenarioId the unique identifier of the scenario.
     */
    @Transaction
    @Query("SELECT * FROM end_condition_table WHERE scenario_id=:scenarioId")
    abstract suspend fun getEndConditionsWithEvent(scenarioId: Long): List<EndConditionWithEvent>

    /**
     * Synchronize the end conditions in the database.
     *
     * @param add the conditions to be added.
     * @param update the conditions to be updated.
     * @param delete the conditions to be deleted.
     */
    @Transaction
    open suspend fun syncEndConditions(
        add: List<EndConditionEntity>,
        update: List<EndConditionEntity>,
        delete: List<EndConditionEntity>,
    ) {
        addEndConditions(add)
        updateEndConditions(update)
        deleteEndConditions(delete)
    }

    /**
     * Add a new end condition to the database.
     *
     * @param endConditionEntities the list of end condition to be added.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addEndConditions(endConditionEntities: List<EndConditionEntity>)

    /**
     * Update the selected end condition.
     *
     * @param endConditionEntities the list of end condition to be updated.
     */
    @Update
    @VisibleForTesting
    abstract suspend fun updateEndConditions(endConditionEntities: List<EndConditionEntity>)

    /**
     * Delete the provided end condition from the database.
     *
     * @param endConditionEntities the list of end condition to be deleted.
     */
    @Delete
    @VisibleForTesting
    abstract suspend fun deleteEndConditions(endConditionEntities: List<EndConditionEntity>)
}