package me.andreaiacono.tessellator.gui

import me.andreaiacono.tessellator.core.*
import me.andreaiacono.tessellator.core.Point
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.border.BevelBorder

import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB


class CanvasPanel(private val main: Main) : JPanel(), MouseListener, MouseMotionListener {

    private var drawGrid: Boolean = true
    private var isOnPoint: Boolean = false
    private var px: Double = 0.0
    private var py: Double = 0.0
    private val n = 5
    private var movingPoint: Point? = null
    var cell = Cell()
    private val image = BufferedImage(4000, 4000, TYPE_INT_RGB)

    init {
        border = BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        addMouseListener(this)
        addMouseMotionListener(this)
    }

    fun reset() {
        cell = Cell()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val imageGraphics = image.createGraphics()
        imageGraphics.color = Color.WHITE
        imageGraphics.fillRect(0, 0, width, height)
        drawCells(imageGraphics as Graphics2D, size.width, size.height, cell)
        g.drawImage(image, 0, 0, null)
    }

    private fun drawCells(g: Graphics2D, canvasWidth: Int, canvasHeight: Int, cell: Cell) {
        val boxWidth = canvasWidth / n
        (0..10).forEach { x ->
            (0..10).forEach { y ->
                drawCell(g, cell, boxWidth, x * boxWidth, y * boxWidth)
            }
        }
    }

    private fun drawCell(g: Graphics2D, cell: Cell, width: Int, left: Int, top: Int) {
        val boxHeight = width

        // draws the original cell
        if (drawGrid) {
            g.stroke = BasicStroke(1.0f)
            g.color = Color.LIGHT_GRAY
            g.drawRect(left + 1, top + 1, width, boxHeight)
        }

        // draws the cell
        g.stroke = BasicStroke(4.0f)
        g.color = Color.BLACK
        val scaledHorizontal = cell.horizontal.map { it.scale(width, boxHeight) }
        val scaledVertical = cell.vertical.map { it.scale(width, boxHeight) }
        for (i in 1 until scaledHorizontal.size) {
            val previous = scaledHorizontal[i - 1]
            val current = scaledHorizontal[i]
            g.drawLine(
                left + previous.x,
                boxHeight + top - previous.y,
                left + current.x,
                boxHeight + top - current.y
            )
        }
        for (i in 1 until scaledVertical.size) {
            val previous = scaledVertical[i - 1]
            val current = scaledVertical[i]
            g.drawLine(
                width + left - previous.x,
                top + previous.y,
                width + left - current.x,
                top + current.y
            )
        }
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
        val x = e!!.x
        val y = e.y
        val size = width / n
        px = (x % size) / size.toDouble()
        py = 1.0 - (y % size) / size.toDouble()

        if (isOnPoint) {
            val existingPoint = cell.findExistingPoint(Point(px, py))
            if (existingPoint != null) {
                existingPoint.isMoving = true
                movingPoint = existingPoint
            } else {
                movingPoint = if (px > py) {
                    cell.addHorizontalPoint(Point(px, py, PointType.HORIZONTAL, true))
                } else {
                    println("adding vertical")
                    cell.addVerticalPoint(Point(px, 1.0 - py, PointType.VERTICAL))
                }
            }
        }
    }

    override fun mouseReleased(e: MouseEvent?) {
        if (movingPoint != null) {
            cell.fixPoint(movingPoint!!)
            movingPoint = null
            repaint()
        }
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mouseDragged(e: MouseEvent?) {
        val x = e!!.x
        val y = e.y
        val size = width / n
        if (movingPoint != null) {
            if (movingPoint!!.pointType == PointType.HORIZONTAL) {
                movingPoint!!.x = (x % size) / size.toDouble()
                movingPoint!!.y = 1.0 - (y % size) / size.toDouble()
            } else {
                movingPoint!!.x = 1.0 - (x % size) / size.toDouble()
                movingPoint!!.y = (y % size) / size.toDouble()
            }
            repaint()
        }
    }

    override fun mouseMoved(e: MouseEvent?) {
        val x = e!!.x
        val y = e.y
        val color = Color(image.getRGB(x, y))
        val size = width / n
        if (movingPoint != null) {
            px = (x % size) / size.toDouble()
            py = (y % size) / size.toDouble()
            movingPoint!!.x = px
            movingPoint!!.y = py
            repaint()
        }
        if (color == Color.BLACK) {
            cursor = Cursor(Cursor.HAND_CURSOR)
            isOnPoint = true
        } else {
            cursor = Cursor(Cursor.DEFAULT_CURSOR)
            isOnPoint = false
        }

    }

    fun setDrawGrid(value: Boolean) {
        drawGrid = value;
        repaint()
    }
}