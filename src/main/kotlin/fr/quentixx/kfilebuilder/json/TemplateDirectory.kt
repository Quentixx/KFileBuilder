package fr.quentixx.kfilebuilder.json

import kotlinx.serialization.*
import java.util.*

/**
 * Represents a template directory with a name, description, and a list of contents.
 */
@Serializable
data class TemplateDirectory(
    val uuid: String = UUID.randomUUID().toString(),
    var name: String = "",
    var description: String = "",
    var content: Node = Node(System.getProperty("user.home"), true)
)
