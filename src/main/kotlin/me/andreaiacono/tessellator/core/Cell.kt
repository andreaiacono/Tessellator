package me.andreaiacono.tessellator.core

import me.andreaiacono.tessellator.core.PointType.HORIZONTAL
import me.andreaiacono.tessellator.core.PointType.VERTICAL
import kotlin.math.abs


class Cell {

    private val EPSILON: Double = 0.001
    var horizontal = mutableListOf(Point(0.0, 0.0, HORIZONTAL), Point(1.0, 0.0, HORIZONTAL))
    var vertical = mutableListOf(Point(0.0, 0.0, VERTICAL), Point(0.0, 1.0, VERTICAL))

    fun findExistingPoint(searchedPoint: Point): Point? {
        horizontal.filter { (abs(it.x - searchedPoint.x) < EPSILON && abs(it.y - searchedPoint.y) < EPSILON) }
            .firstOrNull()
            ?.let {
                return it
            }

        vertical.filter { (abs(it.x - searchedPoint.x) < EPSILON && abs(it.y - searchedPoint.y) < EPSILON) }
            .firstOrNull()
            ?.let {
                return it
            }

        return null
    }


    fun addHorizontalPoint(newPoint: Point): Point {
        horizontal.filter { (abs(it.x - newPoint.x) < EPSILON && abs(it.y - newPoint.y) < EPSILON) }
            .firstOrNull()
            .let {
                if (it == null) {
                    horizontal.add(newPoint)
                    horizontal.sortWith(Comparator.comparingDouble { point -> point.x })
                    return newPoint
                } else {
                    it.isMoving = true
                    return it
                }
            }
    }

    fun addVerticalPoint(newPoint: Point): Point {
        vertical.filter { (abs(it.x - newPoint.x) < EPSILON && abs(it.y - newPoint.y) < EPSILON) }
            .firstOrNull()
            .let {
                if (it == null) {
                    vertical.add(newPoint)
                    vertical.sortWith(Comparator.comparingDouble { point -> point.y })
                    return newPoint
                } else {
                    it.isMoving = true
                    return it
                }
            }
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

