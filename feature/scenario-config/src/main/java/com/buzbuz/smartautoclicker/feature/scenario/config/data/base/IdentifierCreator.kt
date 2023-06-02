 
package com.buzbuz.smartautoclicker.feature.scenario.config.data.base

import com.buzbuz.smartautoclicker.core.domain.model.DATABASE_ID_INSERTION
import com.buzbuz.smartautoclicker.core.domain.model.Identifier

internal class IdentifierCreator {

    /** The last generated domain id for an item. */
    private var lastGeneratedDomainId: Long = 0

    fun generateNewIdentifier(): Identifier =
        Identifier(databaseId = DATABASE_ID_INSERTION, domainId = ++lastGeneratedDomainId)

    fun resetIdCount() {
        lastGeneratedDomainId = 0
    }
}