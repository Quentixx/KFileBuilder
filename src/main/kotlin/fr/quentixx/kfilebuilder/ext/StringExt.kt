package fr.quentixx.kfilebuilder.ext

import fr.quentixx.kfilebuilder.json.TemplateDirectory

/**
 * Knows if the current string value is valid to be a name for a [TemplateDirectory]
 */
fun String.isValidTemplateName() = isNotEmpty()
