package me.andreaiacono.tessellator.gui

import me.andreaiacono.tessellator.core.*
import me.andreaiacono.tessellator.core.Point
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.border.BevelBorder

import java.awt.*
import java.awt.Color.RED
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import java.awt.event.MouseListener
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import kotlin.math.min


class CanvasPanel(private val main: Main) : JPanel(), MouseListener, MouseMotionListener {

    private var thickness: Int = 4
    private var currentX: Int = 0
    private var currentY: Int = 0
    private var drawGrid: Boolean = true
    private var isOnDrawing: Boolean = false
    private var zoom = 5
    private var changingPoint: Point? = null
    private var hoveringPoint: Point? = null
    private var hoveringPixel: Point? = null
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
        val boxWidth = canvasWidth / zoom
        cell.setNewSize(boxWidth)
        (0..zoom).forEach { x ->
            (0..zoom).forEach { y ->
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
        g.stroke = BasicStroke(thickness.toFloat())
        g.color = Color.BLACK
        val scaledHorizontal = cell.points.map { it.scale(width, boxHeight) }
        for (i in 1 until scaledHorizontal.size) {
            val previous = scaledHorizontal[i - 1]
            val current = scaledHorizontal[i]
            g.drawLine(
                left + previous.x,
                top - previous.y,
                left + current.x,
                top - current.y
            )
        }
        if (hoveringPixel != null) {
//            println("Hovering pixel  $hoveringPixel")
            g.color = RED
            val rectSize = 5
            val scaledPoint = hoveringPixel!!.scale(width, boxHeight)
            g.fillRect(left + scaledPoint.x - 2, top - scaledPoint.y - 2, rectSize, rectSize)
        }

        if (hoveringPoint != null) {
//            println("Hovering point  $hoveringPixel")
            val scaledPoint = hoveringPoint!!.scale(width, boxHeight)
            g.color = Color.GREEN
            g.stroke = BasicStroke(2.0f)
            val circleSize = min(width / 5, 20)
            val halfCircleSize = circleSize / 2
            g.drawArc(left + scaledPoint.x - halfCircleSize, top - scaledPoint.y - halfCircleSize, circleSize, circleSize, 0, 360)
        }
    }

    override fun mouseMoved(e: MouseEvent?) {
        val current = e!!.toCoords()
        val color = Color(image.getRGB(current.x, current.y))
        val size = width / zoom
        val currentPoint = Point(current, size)
//        println("Current poin: $currentPoint")
        val existingPoint = cell.findExistingPoint(currentPoint)
        if (existingPoint != null) {
            hoveringPoint = existingPoint
            cursor = Cursor(Cursor.MOVE_CURSOR)
            isOnDrawing = true
        } else {
            hoveringPoint = null
            if (color == Color.BLACK) {
                cursor = Cursor(Cursor.HAND_CURSOR)
                hoveringPixel = currentPoint
//                println("Hovering $hoveringPixel")
                isOnDrawing = true
            } else {
                hoveringPixel = null
                cursor = Cursor(Cursor.DEFAULT_CURSOR)
                isOnDrawing = false
            }
        }
        repaint()
    }

    override fun mouseDragged(e: MouseEvent?) {
        val current = e!!.toCoords()
        val size = width / zoom
        changingPoint?.updatePosition(current, size)
        repaint()
    }

    override fun mousePressed(e: MouseEvent?) {

        if (isOnDrawing) {
            val current = e!!.toCoords()
            val size = width / zoom
            val existingPoint = cell.findExistingPoint(Point(current, size))
            if (existingPoint != null) {
                changingPoint = existingPoint
            } else {
                changingPoint = hoveringPixel
                cell.addHorizontalPoint(changingPoint!!)
                changingPoint!!.isMoving = true
            }
            isOnDrawing = false
        }
    }

    fun setDrawGrid(value: Boolean) {
        drawGrid = value;
        repaint()
    }

    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    fun setThickness(value: Int) {
        thickness = value
        repaint()
    }

    fun setZoom(value: Int) {
        zoom = value;
        repaint()
    }
}
