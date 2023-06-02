 
package com.buzbuz.smartautoclicker.feature.billing

/** Defines the advantages of Smart AutoClicker Pro. */
interface ProModeAdvantage {

    /** Features available with Pro. */
    enum class Feature : ProModeAdvantage {
        ACTION_TYPE_INTENT,
        ACTION_TYPE_TOGGLE_EVENT,
        BACKUP_EXPORT,
        BACKUP_IMPORT,
        CONDITION_THRESHOLD,
        EVENT_STATE,
        SCENARIO_ANTI_DETECTION,
        SCENARIO_DETECTION_QUALITY,
        SCENARIO_END_CONDITIONS,
    }

    /** Limitations for users without Pro. */
    enum class Limitation(val limit: Int) : ProModeAdvantage {
        ACTION_COUNT_LIMIT(5),
        CONDITION_COUNT_LIMIT(2),
        DETECTION_DURATION_MINUTES_LIMIT(10),
        EVENT_COUNT_LIMIT(10),
        SCENARIO_COUNT_LIMIT(2),
    }
}
