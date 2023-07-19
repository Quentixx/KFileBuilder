package fr.quentixx.kfilebuilder.json

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kotlinx.serialization.*

/**
 * Represents a template directory with a name, description, and a list of contents.
 */
@Serializable
data class TemplateDirectory(
    val name: String,
    val description: String = "",
    val content: List<TemplateContent>
)

/**
 * Specifies a model's content type for serialization and deserialization.
 * Uses type info annotation for JSON mapping.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = TemplateSubDir::class, name = "subdirectory"),
    JsonSubTypes.Type(value = TemplateFile::class, name = "file")
)
@Serializable
sealed class TemplateContent {
    abstract val name: String
}

/**
 * Represents a template subdirectory with a name and a list of contents.
 * Inherits from TemplateContent.
 */
@Serializable
data class TemplateSubDir(
    override val name: String,
    val content: List<TemplateContent>
) : TemplateContent()

/**
 * Represents a template file with a name.
 * Inherits from TemplateContent.
 */
@Serializable
data class TemplateFile(
    override val name: String
) : TemplateContent()