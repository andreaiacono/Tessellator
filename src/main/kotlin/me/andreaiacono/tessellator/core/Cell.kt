package me.andreaiacono.tessellator.core

import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.sqrt

@Serializable
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

    fun areInline(a: ScaledPoint, b: ScaledPoint, c: ScaledPoint): Boolean {
        return areEqual(distance(a, b) + distance(b, c), distance(a, c))
    }

    fun distance(a: ScaledPoint, b: ScaledPoint): Double = sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y).toDouble() )

    private fun areEqual(a: Double, b: Double): Boolean {
        return abs(a - b) < (epsilon)
    }

    fun findNewPointIndex(point: ScaledPoint, width: Int): Int? {
        for (i in 1 until points.size) {
            println("are inline ${points[i - 1].scale(width, width)}, $point, ${points[i].scale(width, width)}")
            println("are inline ${points[i - 1].scale(width, width)}, ${point.copy(y = point.y - width - 1)}, ${points[i].scale(width, width)}")
            println("are inline ${points[i - 1].scale(width, width)}, ${point.copy(x = point.x - width - 1)}, ${points[i].scale(width, width)}")

            if (areInline(points[i - 1].scale(width, width), point, points[i].scale(width, width))) {
                println("YESSS  ${points[i - 1].scale(width, width)}, $point, ${points[i].scale(width, width)}")
                return i
            }
            else if (areInline(points[i - 1].scale(width, width), point.copy(y = point.y - width - 1), points[i].scale(width, width)) ) {
                println("YESSS COPY  ${points[i - 1].scale(width, width)}, ${point.copy(y = point.y - width - 1)}, ${points[i].scale(width, width)}")
                return i
            }
            else if (areInline(points[i - 1].scale(width, width), point.copy(x = point.x - width - 1), points[i].scale(width, width)) ) {
                println("YESSS COPY  ${points[i - 1].scale(width, width)}, ${point.copy(x = point.x - width - 1)}, ${points[i].scale(width, width)}")
                return i
            }
        }
        return null
    }

    fun addHorizontalPoint(newPoint: Point, width: Int) {
        val index = findNewPointIndex(newPoint.scale(width, width), width) ?: throw Exception("Trying to add a new point not part of a line")
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
