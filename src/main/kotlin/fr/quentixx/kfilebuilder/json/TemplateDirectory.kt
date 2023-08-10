package fr.quentixx.kfilebuilder.json

import kotlinx.serialization.*

/**
 * Represents a template directory with a name, description, and a list of contents.
 */
@Serializable
data class TemplateDirectory(
    var name: String,
    var description: String = "",
    var content: Node
)
