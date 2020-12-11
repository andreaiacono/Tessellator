package me.andreaiacono.tessellator.core


class Cell {

    var horizontal = mutableListOf(Point(0.0,0.0), Point(1.0, 0.0))
    var vertical = mutableListOf( Point(0.0,0.0), Point(0.0, 1.0) )

    fun addHorizontalPoint(newPoint: Point) {
        horizontal.add(newPoint)
        horizontal.sortWith(Comparator.comparingDouble { point ->  point.x })
    }

    fun addVerticalPoint(newPoint: Point) {
        vertical.add(newPoint)
        vertical.sortWith(Comparator.comparingDouble { point ->  point.y })
    }


}

data class ScaledPoint(val x: Int, val y: Int)

data class Point(val x: Double, val y: Double)
fun Point.scale(width: Int, height:Int): ScaledPoint = ScaledPoint((this.x * width).toInt(), (this.y * height).toInt())

