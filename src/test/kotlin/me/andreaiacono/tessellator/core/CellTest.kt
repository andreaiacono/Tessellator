package me.andreaiacono.tessellator.core

import org.junit.Test
import kotlin.test.assertEquals

internal class CellTest {

    @Test
    fun addHorizontalPoint() {
    }

    @Test
    fun addVerticalPoint() {
        val cell = Cell()
        assertEquals(3, cell.points.size)
    }
}