 
package com.buzbuz.smartautoclicker.feature.scenario.debugging.domain

data class ProcessingDebugInfo internal constructor(
    val processingCount: Long = 0,
    val successCount: Long = 0,
    val totalProcessingTimeMs: Long = 0,
    val avgProcessingTimeMs: Long = 0,
    val minProcessingTimeMs: Long = 0,
    val maxProcessingTimeMs: Long = 0,
)
