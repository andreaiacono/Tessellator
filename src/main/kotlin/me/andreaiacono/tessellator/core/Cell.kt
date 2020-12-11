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
        horizontal.add(newPoint)
        horizontal.sortWith(Comparator.comparingDouble { point -> point.x })
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

data class Point(var x: Double, var y: Double, val pointType: PointType? = null, var isMoving: Boolean = false)

fun Point.scale(width: Int, height: Int): ScaledPoint = ScaledPoint((this.x * width).toInt(), (this.y * height).toInt())

