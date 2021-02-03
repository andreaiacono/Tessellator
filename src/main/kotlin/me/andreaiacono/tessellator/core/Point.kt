package me.andreaiacono.tessellator.core

import kotlinx.serialization.Serializable

@Serializable
data class Point(var x: Double, var y: Double, var isMoving: Boolean = false) {

    constructor(coords: Coords, size: Int) : this(
        (coords.x % size) / size.toDouble(),
        1.0 - (coords.y % size) / size.toDouble()
    )

    fun updatePosition(current: Coords, starting: Coords, size: Int) {
        x = (starting.x %size) / size.toDouble() + (((current.x - starting.x))) / size.toDouble()
        if (x > 1) {
            x -= 1
        }
        y = 1.0 - (starting.y %size) / size.toDouble() - (((current.y - starting.y))) / size.toDouble()
        if (y > 1) {
            y -= 1
        }
    }

    fun scale(width: Int, height: Int): ScaledPoint = ScaledPoint((this.x * width).toInt(), (this.y * height).toInt())
}

data class ScaledPoint(val x: Int, val y: Int)


