 
package com.buzbuz.smartautoclicker.core.domain.model

/**
 * Identifier for all model objects stored in database.
 * In order to ease the user creation and edition of those structure, it stores 2 identifiers:
 *
 * @param databaseId: the identifier in the database. 0 if this object is not yet inserted, in that case, the domainId must
 * be defined.
 * @param domainId: the identifier for the domain, set only when the object is not yet inserted in database.
 */
data class Identifier(
    val databaseId: Long = DATABASE_ID_INSERTION,
    val domainId: Long? = null,
) {

    internal constructor(id: Long, asDomain: Boolean = false) : this(
        databaseId = if (asDomain) DATABASE_ID_INSERTION else id,
        domainId = if (asDomain) id else null,
    )

    /** Ensure correctness of ids. */
    init {
        if (databaseId == DATABASE_ID_INSERTION && domainId == null)
            throw IllegalArgumentException("DomainId must be set when using db id 0")

        if (databaseId != DATABASE_ID_INSERTION && domainId != null)
                throw IllegalArgumentException("Both ids can't be set")
    }

    internal fun isInDatabase(): Boolean = databaseId != DATABASE_ID_INSERTION
}

/** Value to set to [Identifier.databaseId] when willing to insert an item in database. */
const val DATABASE_ID_INSERTION = 0L