package fr.quentixx.kfilebuilder.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * Represents a file node that can contain other nodes.
 * This allows you to build an abstract file architecture.
 */
@Serializable
data class Node(
    var path: String,
    val isDirectory: Boolean,
    val children: MutableList<Node> = mutableListOf(),
    @Transient
    var lastUpdated : Long = System.currentTimeMillis() // For node builder system handling
)
