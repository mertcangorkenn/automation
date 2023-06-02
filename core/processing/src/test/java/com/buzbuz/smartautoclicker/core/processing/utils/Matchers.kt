 
package com.buzbuz.smartautoclicker.core.processing.utils

import org.mockito.Mockito

/**
 * Argument matcher for a nonnull kotlin function argument matching any nonnull value.
 * @param T the type of the argument.
 */
fun <T> anyNotNull(): T = Mockito.any()