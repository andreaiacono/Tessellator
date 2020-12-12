package me.andreaiacono.tessellator.core

import java.awt.event.MouseEvent

data class Coords (val x: Int, val y: Int) {
    override fun toString() = "($x,$y)"
}

fun MouseEvent.toCoords() = Coords(this.x, this.y)
