package me.andreaiacono.tessellator.core

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class FileData(
    val cell: Cell,
    val zoom: Int = 5,
    val lineThickness: Int = 3,
    val drawGrid: Boolean = true,
    val drawColor: Boolean = false,
    val color1: Int = 16777215,
    val color2: Int = 13158600
)

fun FileData.serialize(): String = Json.encodeToString(FileData.serializer(), this)

fun deserialize(json: String) = Json.decodeFromString(FileData.serializer(), json)
