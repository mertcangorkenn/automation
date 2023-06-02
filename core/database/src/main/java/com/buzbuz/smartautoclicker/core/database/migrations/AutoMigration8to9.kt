 
package com.buzbuz.smartautoclicker.core.database.migrations

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

/**
 * Migration from database v8 to v9.
 *
 * Changes:
 * * removes the stop_after value from event_table. It was unused.
 * * add randomize column to scenario_table.
 * * add toggle_event_id and toggle_type to action_table.
 */
@DeleteColumn(tableName = "event_table", columnName = "stop_after")
class AutoMigration8to9 : AutoMigrationSpec