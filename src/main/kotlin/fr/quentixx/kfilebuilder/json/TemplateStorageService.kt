package fr.quentixx.kfilebuilder.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import java.io.File
import com.fasterxml.jackson.module.kotlin.readValue

private val logger = KotlinLogging.logger {}

/**
 * Represents the service to store and retrieve templates configuration data.
 */
object TemplateStorageService {

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
     * Saves the provided template to the storage.
     * If the template already exists by his UUID, it will be changed, otherwise it will be created.
     * @param template The [TemplateDirectory] to save.
     */
    fun save(template: TemplateDirectory) {
        logger.info { "Saving template ${template.name}" }
        val existingTemplates = getAll()
        val updatedTemplates = existingTemplates.let {

            val existingTemplate = it.firstOrNull { it.uuid == template.uuid }
            if (existingTemplate == null) {
                println("Saving new template: ${template.name}")
                (it + template)
            } else {
                println("Saving existing template: ${template.name}")
                existingTemplate.apply {
                    name = template.name
                    description = template.description
                    content = template.content
                }
                it
            }

        }
        saveAll(updatedTemplates)
    }

    fun isExistsByUuid(templateUuid: String): Boolean {
        return getAll().any { it.uuid == templateUuid }
    }

    fun isExistsByName(templateName: String): Boolean {
        return getAll().any { it.name == templateName }
    }

    /**
     * Deletes the specified template from the storage.
     * @param templateUuid The UUID of the template to be deleted.
     */
    fun delete(templateUuid: String) {
        logger.info { "Deleting template with UUID $templateUuid" }
        val updatedTemplates = getAll().filter { it.uuid != templateUuid }
        saveAll(updatedTemplates)
    }
}
