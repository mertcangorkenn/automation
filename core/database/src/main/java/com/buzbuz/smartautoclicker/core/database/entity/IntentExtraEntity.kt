 
package com.buzbuz.smartautoclicker.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable

/**
 * Entity defining an intent extra.
 *
 * An intent extra is linked with an action of type [ActionType.INTENT] and contains one of the extra to put in the
 * [android.content.Intent] created when the action is executed.
 *
 * @param id unique identifier for an event. Also the primary key in the table.
 * @param actionId unique identifier of this extra's action. Reference the key [ActionEntity.id] in scenario_table.
 * @param type the type of the value for this extra.
 * @param key the key of the extra.
 * @param value the value for the extra. This is a string representation of the value with the type [type].
 */
@Entity(
    tableName = "intent_extra_table",
    indices = [Index("action_id")],
    foreignKeys = [ForeignKey(
        entity = ActionEntity::class,
        parentColumns = ["id"],
        childColumns = ["action_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
@Serializable
data class IntentExtraEntity(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "action_id") var actionId: Long,
    @ColumnInfo(name = "type") val type: IntentExtraType,
    @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "value") val value: String,
)

/** The list of supported types for the value of an [IntentExtraEntity]. */
enum class IntentExtraType {
    BOOLEAN,
    BYTE,
    CHAR,
    DOUBLE,
    INTEGER,
    FLOAT,
    SHORT,
    STRING
}

/** Type converter to read/write the [IntentExtraType] into the database. */
internal class IntentExtraTypeStringConverter {
    @TypeConverter
    fun fromString(value: String): IntentExtraType = IntentExtraType.valueOf(value)
    @TypeConverter
    fun toString(intentExtraType: IntentExtraType): String = intentExtraType.toString()
}