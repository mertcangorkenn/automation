 
package com.buzbuz.smartautoclicker.core.processing.domain

/** The different states of the detection. */
enum class DetectionState {
    /** The detection is inactive. */
    INACTIVE,
    /** The screen is being recorded. */
    RECORDING,
    /** The screen is being recorded and the detection is running. */
    DETECTING,
}