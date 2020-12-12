package me.andreaiacono.tessellator.core

import me.andreaiacono.tessellator.core.PointType.HORIZONTAL
import java.text.DecimalFormat
import kotlin.math.abs


class Cell {

    var size: Int = 1
    var epsilon: Double = 0.1
    var points = mutableListOf(Point(0.0, 0.0, HORIZONTAL), Point(1.0, 0.0, HORIZONTAL), Point(1.0, 1.0, HORIZONTAL))

    fun findExistingPoint(searchedPoint: Point): Point? {
        points.filter { (abs(it.x - searchedPoint.x) < epsilon && abs(it.y - searchedPoint.y) < epsilon) }
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
        return abs(a - b) < (epsilon * 2)
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
}

data class ScaledPoint(val x: Int, val y: Int)
enum class PointType {
    HORIZONTAL, VERTICAL
}

data class Point(var x: Double, var y: Double, val pointType: PointType? = null, var isMoving: Boolean = false) {
    val dec = DecimalFormat("#.####")
    override fun toString(): String = "[${pointType.toString()[0]}(${dec.format(x)}, ${dec.format(y)}]"

    constructor(coords: Coords, size: Int) : this(
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

