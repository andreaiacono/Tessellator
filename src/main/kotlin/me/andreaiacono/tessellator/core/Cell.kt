package me.andreaiacono.tessellator.core

import java.text.DecimalFormat
import kotlin.math.abs

class Cell {

    var size: Int = 1
    var epsilon: Double = 0.1
    var points = mutableListOf(Point(0.0, 0.0), Point(1.0, 0.0), Point(1.0, 1.0))

    fun findExistingPoint(searchedPoint: Point): Point? {
        points.filter { (abs(it.x - searchedPoint.x) < epsilon * 4 && abs(it.y - searchedPoint.y) < epsilon * 4)  }
            .firstOrNull()
            ?.let {
                return it
            }
        return null
    }

    fun areInline(a: Point, b: Point, c: Point): Boolean {

        // vertical
        if (areEqual(a.x, c.x)) {
            return areEqual(b.x, c.x)
        }

        // horizontal
        if (areEqual(a.y, c.y)) {
            return areEqual(b.y, c.y)
        }

        // match the gradients
        return areEqual((a.x - c.x) * (a.y - c.y), (c.x - b.x) * (c.y - b.y))
    }

    fun areEqual(a: Double, b: Double): Boolean {
        return abs(a - b) < (epsilon * 4)
    }

    fun findNewPointIndex(point: Point): Int? {
        for (i in 1 until points.size) {
            if (areInline(point, points[i - 1], points[i])) {
                return i
            }
        }
        return null
    }

    fun addHorizontalPoint(newPoint: Point) {
        println("before adding $newPoint to $points")
        val index = findNewPointIndex(newPoint) ?: throw Exception("Trying to add a new point not part of a line")
        points.add(index, newPoint)
        println("after adding $points")
    }

    fun setNewSize(size: Int) {
        this.size = size
        epsilon = 5 / size.toDouble()
    }

    fun delete(point: Point) {
        points.filter { (abs(it.x - point.x) < epsilon && abs(it.y - point.y) < epsilon)  }
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

