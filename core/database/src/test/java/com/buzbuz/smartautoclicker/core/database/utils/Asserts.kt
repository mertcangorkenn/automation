 
package com.buzbuz.smartautoclicker.core.database.utils

import android.database.Cursor
import org.junit.Assert.assertEquals

/**
 * Verify that the content of two lists are the same, in any order.
 *
 * @param T the type of the items in the lists.
 * @param expectedItems the list of expected items.
 * @param actualItems the list of actual items.
 * @param identifierProvider callback providing the identifier to match the items from actual and expected lists
 *                           before verifying their equality.
 */
fun <T> assertSameContent(expectedItems: List<T>, actualItems: List<T>, identifierProvider: (T) -> Long) {
    actualItems.forEach { actualItem ->
        val matchedExpected = expectedItems.find { expectedItem ->
            identifierProvider(expectedItem) == identifierProvider(actualItem)
        }

        assertEquals(matchedExpected, actualItem)
    }
}

/** Check the number of items in a cursor. */
fun Cursor.assertCountEquals(expectedCount: Int) =
    assertEquals("Invalid list size", expectedCount, count)

fun Cursor.assertColumnEquals(expected: Long, actualColumn: String) =
    assertEquals(
        "Invalid column value for $actualColumn",
        expected,
        getLong(getColumnIndex(actualColumn))
    )
