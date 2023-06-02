 
package com.buzbuz.smartautoclicker.core

import android.graphics.Point
import android.graphics.Rect

/**
 * Get the width and height of this Rect.
 *
 * @return the size.
 */
fun Rect.size(): Point = Point(width(), height())