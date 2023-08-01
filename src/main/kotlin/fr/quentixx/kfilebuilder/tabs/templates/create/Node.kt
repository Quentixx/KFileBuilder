package fr.quentixx.kfilebuilder.tabs.templates.create

data class Node(
    var path: String,
    val isDirectory: Boolean,
    val children: MutableList<Node> = mutableListOf(),
    var lastUpdated : Long = System.currentTimeMillis() // For node builder system handling
)