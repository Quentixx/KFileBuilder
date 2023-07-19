package fr.quentixx.kfilebuilder.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import java.io.File
import com.fasterxml.jackson.module.kotlin.readValue

private val logger = KotlinLogging.logger { }

/**
 * Represents the service to store and retrieve templates configuration data.
 */
object TemplatesService {

    private val mapper = jacksonObjectMapper()
    private val file = File("templates.json")

    init {
        if (!file.exists()) {
            logger.info { "Templates storage file not found, creating new file `${file.name}`" }

            file.createNewFile()

            saveAll(emptyList()) // Initialize the JSON file with an empty list
        }
    }

    /**
     * Retrieves all templates from the storage.
     *
     * @return List of [TemplateDirectory] objects.
     */
    fun getAll(): List<TemplateDirectory> {
        val jsonArray = file.readText()
        return mapper.readValue(jsonArray)
    }

    /**
     * Saves a template to the storage.
     *
     * @param template The [TemplateDirectory] object to be saved.
     */
    fun save(template: TemplateDirectory) {
        logger.info { "Saving template ${template.name}" }
        val existingTemplates = getAll()
        val updatedTemplates = existingTemplates + template
        saveAll(updatedTemplates)
    }

    /**
     * Saves all templates to the storage.
     *
     * @param templates The list of [TemplateDirectory] objects to be saved.
     */
    private fun saveAll(templates: List<TemplateDirectory>) {
        logger.info { "Saving all templates" }
        val jsonString = mapper.writeValueAsString(templates)
        file.writeText(jsonString)
    }

    /**
     * Checks if a template with the specified name exists in the storage.
     * @param templateName The name of the template to check.
     * @return True if the template exists, False otherwise.
     */
    fun isExists(templateName: String): Boolean {
        return getAll().any { it.name == templateName }
    }
}
