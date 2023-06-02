 
package com.buzbuz.smartautoclicker.feature.backup.data.ext

import java.io.File
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Write a file into the zip stream.
 * @param entry the file to be zipped.
 */
internal fun ZipOutputStream.writeEntryFile(entry: File) {
    entry.inputStream().use { input ->
        input.copyTo(this)
    }
}

/**
 * Read a file into from this zip stream.
 * @param output the file to put the content into.
 */
internal fun ZipInputStream.readEntryFile(output: File) =
    output.apply {
        outputStream().use { output ->
            copyTo(output)
        }
    }