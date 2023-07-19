package fr.quentixx.kfilebuilder.tabs.templates.create

data class Node(
    var name: String,
    val isFolder: Boolean,
    val parent : Node? = null,
    val children: List<Node> = emptyList()
)