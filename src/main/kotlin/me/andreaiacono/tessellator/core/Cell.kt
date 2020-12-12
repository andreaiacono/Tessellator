package me.andreaiacono.tessellator.core

import me.andreaiacono.tessellator.core.PointType.HORIZONTAL
import me.andreaiacono.tessellator.core.PointType.VERTICAL
import kotlin.math.abs


class Cell {

    var horizontal = mutableListOf(Point(0.0, 0.0, HORIZONTAL), Point(1.0, 0.0, HORIZONTAL))
    var vertical = mutableListOf(Point(0.0, 0.0, VERTICAL), Point(0.0, 1.0, VERTICAL))

    fun findExistingPoint(searchedPoint: Point, size: Int): Point? {
        val epsilon = 5 / size.toDouble()
        horizontal.filter { (abs(it.x - searchedPoint.x) < epsilon && abs(it.y - searchedPoint.y) < epsilon) }
            .firstOrNull()
            ?.let {
                return it
            }

        vertical.filter { (abs(it.x - searchedPoint.x) < epsilon && abs(it.y - searchedPoint.y) < epsilon) }
            .firstOrNull()
            ?.let {
                return it
            }

        return null
    }


    fun addHorizontalPoint(newPoint: Point) {
        println("before add $horizontal")
        horizontal.add(newPoint)
        println("before sort $horizontal")
        horizontal.sortWith(Comparator.comparingDouble { point -> point.x })
        println("after sort $horizontal")
    }

    fun addVerticalPoint(newPoint: Point) {
        vertical.add(newPoint)
        vertical.sortWith(Comparator.comparingDouble { point -> point.y })
    }

    fun fixPoint(movingPoint: Point) {
        movingPoint.isMoving = false
    }

}

data class ScaledPoint(val x: Int, val y: Int)
enum class PointType {
    HORIZONTAL, VERTICAL
}

data class Point(var x: Double, var y: Double, val pointType: PointType? = null, var isMoving: Boolean = false) {
    override fun toString(): String = "[${pointType.toString()[0]}($x, $y)]"

    constructor(coords: Coords, size: Int): this(
        (coords.x % size) / size.toDouble(),
        1.0 - (coords.y % size) / size.toDouble()
    )
}

fun Point.updatePosition(coords: Coords, size: Int) {
    if (this.pointType == HORIZONTAL) {
        this.x = (coords.x % size) / size.toDouble()
        this.y = 1.0 - (coords.y % size) / size.toDouble()
    } else {
        this.x = 1.0 - (coords.x % size) / size.toDouble()
        this.y = (coords.y % size) / size.toDouble()
    }
}

fun Point.scale(width: Int, height: Int): ScaledPoint = ScaledPoint((this.x * width).toInt(), (this.y * height).toInt())

