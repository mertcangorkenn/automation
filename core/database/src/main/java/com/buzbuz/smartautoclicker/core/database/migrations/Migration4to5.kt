 
package com.buzbuz.smartautoclicker.core.database.migrations

import androidx.annotation.VisibleForTesting
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from database v4 to v5.
 *
 * Refactor of the condition table.
 *
 * Changes:
 * * add a name to the conditions.
 * * add the detection type to the conditions. Default must be EXACT (1), as it was the only behaviour previously.
 * * increase the threshold of existing condition by [THRESHOLD_INCREASE] in order to comply with the new algo.
 */
object Migration4to5 : Migration(4, 5) {

    /** Value to be added to current threshold in the database. */
    @VisibleForTesting
    internal const val THRESHOLD_INCREASE = 3
    /** Maximum value for the new threshold. */
    @VisibleForTesting
    internal const val THRESHOLD_MAX_VALUE = 20

    override fun migrate(database: SupportSQLiteDatabase) {
        database.apply {
            execSQL(addConditionNameColumn)
            execSQL(addConditionDetectionTypeColumn)
            updateAllThreshold()
        }
    }

    private fun SupportSQLiteDatabase.updateAllThreshold() {
        query(getAllConditions).use { cursor ->
            if (cursor.count == 0) {
                return
            }

            cursor.moveToFirst()

            val idColumnIndex = cursor.getColumnIndex("id")
            val thresholdColumnIndex = cursor.getColumnIndex("threshold")
            if (idColumnIndex< 0 || thresholdColumnIndex < 0) throw IllegalStateException("Can't find columns")

            do {
                val newThreshold = (cursor.getInt(thresholdColumnIndex) + THRESHOLD_INCREASE)
                    .coerceAtMost(THRESHOLD_MAX_VALUE)
                execSQL(updateThreshold(cursor.getLong(idColumnIndex), newThreshold))
            } while (cursor.moveToNext())
        }
    }

    /** Update the current condition threshold to keep detecting old conditions. */
    private fun updateThreshold(id: Long, threshold: Int) = """
        UPDATE `condition_table` 
        SET `threshold` = $threshold
        WHERE `id` = $id
    """.trimIndent()

    /** Add the name column to the condition table. */
    private val addConditionNameColumn = """
        ALTER TABLE `condition_table` 
        ADD COLUMN `name` TEXT DEFAULT "Condition" NOT NULL
    """.trimIndent()

    /** Add the detection type column to the condition table. */
    private val addConditionDetectionTypeColumn = """
        ALTER TABLE `condition_table` 
        ADD COLUMN `detection_type` INTEGER DEFAULT 1 NOT NULL
    """.trimIndent()

    private val getAllConditions = """
        SELECT `id`, `threshold` 
        FROM `condition_table`
    """.trimIndent()
}