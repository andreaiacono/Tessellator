package me.andreaiacono.tessellator.core

import kotlin.math.abs


class Cell {

    private val EPSILON: Double = 0.1
    var horizontal = mutableListOf(Point(0.0, 0.0), Point(1.0, 0.0))
    var vertical = mutableListOf(Point(0.0, 0.0), Point(0.0, 1.0))

    fun addHorizontalPoint(newPoint: Point) : Point {
        horizontal.filter { (abs(it.x - newPoint.x) < EPSILON && abs(it.y - newPoint.y) < EPSILON) }
            .firstOrNull()
            .let {
                if (it == null) {
                    horizontal.add(newPoint)
                    horizontal.sortWith(Comparator.comparingDouble { point -> point.x })
                    return newPoint
                } else {
                    it.isMoving = true
                    println("FOUND")
                    return it
                }
            }
    }

    fun addVerticalPoint(newPoint: Point) {
        if (vertical.contains(newPoint.copy(isMoving = false))) {
            vertical.filter { abs(it.x - newPoint.x) < EPSILON && abs(it.y - newPoint.y) < EPSILON }
                .firstOrNull()
                ?.let { it.isMoving = true }
        } else {
            vertical.add(newPoint)
            vertical.sortWith(Comparator.comparingDouble { point -> point.y })
        }
    }

    fun fixPoint(movingPoint: Point) {
        movingPoint.isMoving = false
    }
}

data class ScaledPoint(val x: Int, val y: Int)

data class Point(var x: Double, var y: Double, var isMoving: Boolean = false)

fun Point.scale(width: Int, height: Int): ScaledPoint = ScaledPoint((this.x * width).toInt(), (this.y * height).toInt())

