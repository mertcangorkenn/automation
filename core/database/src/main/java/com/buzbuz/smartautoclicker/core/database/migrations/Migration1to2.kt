 
package com.buzbuz.smartautoclicker.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Migration from database v1 to v2.
 * Changes: conditions have now a threshold, allowing to detect images close to the detection one.
 */
object Migration1to2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE condition_table ADD COLUMN threshold INTEGER DEFAULT 1 NOT NULL")
    }
}