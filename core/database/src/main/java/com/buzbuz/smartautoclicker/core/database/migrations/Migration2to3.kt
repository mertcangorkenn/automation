 
package com.buzbuz.smartautoclicker.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from database v2 to v3.
 * Changes: clicks have now an optional amount of executions before the scenario is stopped.
 */
object Migration2to3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE click_table ADD COLUMN stop_after INTEGER DEFAULT NULL")
    }
}