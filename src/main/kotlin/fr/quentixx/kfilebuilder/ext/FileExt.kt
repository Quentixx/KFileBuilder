package fr.quentixx.kfilebuilder.ext

import java.io.File

/**
 * File extension function to know if this file does not contain any directory.
 *
 * @return true if the file does not contain any directory, false otherwise.
 */
fun File.hasNoSubDirectories(): Boolean {
    val files = listFiles()
    return files == null || files.isEmpty() || files.all { !it.isDirectory }
}
