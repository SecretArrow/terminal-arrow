package com.terminalarrow.app.ui

data class VirtualFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val isArchive: Boolean = false,
    val parentArchivePath: String? = null
)
