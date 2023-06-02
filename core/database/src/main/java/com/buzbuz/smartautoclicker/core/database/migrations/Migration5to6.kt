 
package com.buzbuz.smartautoclicker.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.buzbuz.smartautoclicker.core.database.entity.ActionType

/**
 * Migration from database v5 to v6.
 *
 * Changes:
 * * add clickOnCondition to the action click table.
 * * add shouldBeDetected to the condition table in order to allow condition negation.
 */
object Migration5to6 : Migration(5, 6) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.apply {
            execSQL(addConditionShouldBeDetectedColumn)
            execSQL(addActionClickOnConditionColumn)
            updateAllActions()
        }
    }

    /**
     * Update all actions with the new click on condition value.
     * This value must be set for clicks only. As this feature wasn't implemented before this version, the default value
     * must be 0, or false.
     */
    private fun SupportSQLiteDatabase.updateAllActions() {
        query(getAllActions).use { cursor ->
            if (cursor.count == 0) {
                return
            }
            cursor.moveToFirst()

            val idColumnIndex = cursor.getColumnIndex("id")
            val typeColumnIndex = cursor.getColumnIndex("type")
            if (idColumnIndex< 0 || typeColumnIndex < 0) throw IllegalStateException("Can't find columns")

            do {
                val type = ActionType.valueOf(cursor.getString(typeColumnIndex))
                if (type == ActionType.CLICK) {
                    execSQL(updateClickOnCondition(cursor.getLong(idColumnIndex), 0))
                }
            } while (cursor.moveToNext())
        }
    }

    /** Add the should be detected column to the condition table. */
    private val addConditionShouldBeDetectedColumn = """
        ALTER TABLE `condition_table` 
        ADD COLUMN `shouldBeDetected` INTEGER DEFAULT 1 NOT NULL
    """.trimIndent()

    /** Add the click on condition to the action table. */
    private val addActionClickOnConditionColumn = """
        ALTER TABLE `action_table` 
        ADD COLUMN `clickOnCondition` INTEGER
    """.trimIndent()

    /** Update the click on condition value in the action table. */
    private fun updateClickOnCondition(id: Long, clickOnCondition: Int) = """
        UPDATE `action_table` 
        SET `clickOnCondition` = $clickOnCondition
        WHERE `id` = $id
    """.trimIndent()

    /** Get all actions on the table. */
    private val getAllActions = """
        SELECT `id`, `type` 
        FROM `action_table`
    """.trimIndent()
}