package me.andreaiacono.tessellator.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FileData(
    val cell: Cell,
    val zoom: Int,
    val drawGrid: Boolean,
    val drawColor: Boolean
)

fun FileData.serialize(): String = Json.encodeToString(FileData.serializer(), this)

fun deserialize(json: String) = Json.decodeFromString(FileData.serializer(), json)
