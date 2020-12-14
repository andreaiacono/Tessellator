package me.andreaiacono.tessellator.core

import java.text.DecimalFormat
import kotlin.math.abs
import kotlin.math.sqrt

class Cell {

    var size: Int = 1
    var epsilon: Double = 0.1
    var points = mutableListOf(Point(0.0, 0.0), Point(1.0, 0.0), Point(1.0, 1.0))

    fun findExistingPoint(searchedPoint: Point): Point? {
        points.filter { (abs(it.x - searchedPoint.x) < epsilon && abs(it.y - searchedPoint.y) < epsilon) }
            .firstOrNull()
            ?.let {
                return it
            }
        return null
    }

    fun areInline(a: Point, b: Point, c: Point): Boolean {
        return areEqual(distance(a, b) + distance(b, c), distance(a, c))
    }

    fun distance(a: Point, b: Point): Double = sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) )

    private fun areEqual(a: Double, b: Double): Boolean {
        return abs(a - b) < (epsilon)
    }

    fun findNewPointIndex(point: Point): Int? {
        for (i in 1 until points.size) {
            if (areInline(points[i - 1], point, points[i])) {
                return i
            }
        }
        return null
    }

    fun addHorizontalPoint(newPoint: Point) {
        val index = findNewPointIndex(newPoint) ?: throw Exception("Trying to add a new point not part of a line")
        points.add(index, newPoint)
    }

    fun setNewSize(size: Int) {
        this.size = size
        epsilon = 5 / size.toDouble()
    }

    fun delete(point: Point) {
        points.filter { (abs(it.x - point.x) < epsilon && abs(it.y - point.y) < epsilon) }
            .firstOrNull()
            ?.let {
                points.remove(it)
            }
    }
}

data class ScaledPoint(val x: Int, val y: Int)
data class Point(var x: Double, var y: Double, var isMoving: Boolean = false) {
    val dec = DecimalFormat("#.####")
    override fun toString(): String = "(${dec.format(x)}, ${dec.format(y)})"

    constructor(coords: Coords, size: Int) : this(
        (coords.x % size) / size.toDouble(),
        1.0 - (coords.y % size) / size.toDouble()
    )

    fun updatePosition(coords: Coords, size: Int) {
        x = (coords.x % size) / size.toDouble()
        y = 1.0 - (coords.y % size) / size.toDouble()
    }
}


fun Point.scale(width: Int, height: Int): ScaledPoint = ScaledPoint((this.x * width).toInt(), (this.y * height).toInt())

