package me.andreaiacono.tessellator.gui

import me.andreaiacono.tessellator.core.*
import me.andreaiacono.tessellator.core.Point
import me.andreaiacono.tessellator.core.PointType.HORIZONTAL
import me.andreaiacono.tessellator.core.PointType.VERTICAL
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

    private var currentX: Int = 0
    private var currentY: Int = 0
    private var drawGrid: Boolean = true
    private var isOnDrawing: Boolean = false
    private val n = 5
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
                top - previous.y,
                left + current.x,
                top - current.y
            )
        }
        for (i in 1 until scaledVertical.size) {
            val previous = scaledVertical[i - 1]
            val current = scaledVertical[i]
            g.drawLine(
                left - previous.x,
                top + previous.y,
                left - current.x,
                top + current.y
            )
        }
        if (isOnDrawing) {
            g.color = RED
            val rectSize = 5
            val size = width
            val px = (currentX % size) / size.toDouble()
            val py = 1.0 - (currentY % size) / size.toDouble()
            val scaledPoint = Point(px, py).scale(width, boxHeight)
            g.fillRect(left + scaledPoint.x - 2, top - scaledPoint.y - 2, rectSize, rectSize)
        }

        if (hoveringPoint != null) {
            val scaledPoint = hoveringPoint!!.scale(width, boxHeight)
            g.color = Color.GREEN
            g.stroke = BasicStroke(2.0f)
            val circleSize = min(width / 5, 20)
            val halfCircleSize = circleSize / 2
            g.drawArc(left + scaledPoint.x - halfCircleSize, top - scaledPoint.y - halfCircleSize, circleSize, circleSize, 0, 360)
        }
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
        if (changingPoint != null) {
            cell.fixPoint(changingPoint!!)
            changingPoint = null
            repaint()
        }
    }

    override fun mousePressed(e: MouseEvent?) {
        val x = e!!.x
        val y = e.y
        val size = width / n
        val px = (x % size) / size.toDouble()
        val py = 1.0 - (y % size) / size.toDouble()

        if (isOnDrawing) {
            val existingPoint = cell.findExistingPoint(Point(px, py), width)
            if (existingPoint != null) {
                changingPoint = existingPoint
            } else {
                changingPoint = hoveringPixel
                if (changingPoint!!.pointType == HORIZONTAL) {
                    cell.addHorizontalPoint(changingPoint!!)
                } else {
                    cell.addVerticalPoint(changingPoint!!)
                }
                changingPoint!!.isMoving = true
            }
            isOnDrawing = false
        }
    }


    override fun mouseDragged(e: MouseEvent?) {
        val x = e!!.x
        val y = e.y
        val size = width / n
        if (changingPoint != null) {
            if (changingPoint!!.pointType == HORIZONTAL) {
                changingPoint!!.x = (x % size) / size.toDouble()
                changingPoint!!.y = 1.0 - (y % size) / size.toDouble()
            } else {
                changingPoint!!.x = 1.0 - (x % size) / size.toDouble()
                changingPoint!!.y = (y % size) / size.toDouble()
            }
            repaint()
        }
    }

    override fun mouseMoved(e: MouseEvent?) {
        currentX = e!!.x
        currentY = e.y
        val color = Color(image.getRGB(currentX, currentY))
        val size = width / n
        val px = 1.0 - (currentX % size) / size.toDouble()
        val py = (currentY % size) / size.toDouble()

        if (changingPoint != null) {
            changingPoint!!.x = px
            changingPoint!!.y = py
        }
        else {
            val existingPoint = cell.findExistingPoint(Point(px, py), width)
            if (existingPoint != null) {
                hoveringPoint = existingPoint
                cursor = Cursor(Cursor.MOVE_CURSOR)
                isOnDrawing = true
            } else {
                hoveringPoint = null
                if (color == Color.BLACK) {
                    cursor = Cursor(Cursor.HAND_CURSOR)
                    val pointType = if (currentY.rem(width) < currentX.rem(width)) VERTICAL else HORIZONTAL
                    hoveringPixel = Point(px, py, pointType);
                    println(hoveringPixel!!.pointType)
                    isOnDrawing = true
                } else {
                    cursor = Cursor(Cursor.DEFAULT_CURSOR)
                    isOnDrawing = false
                }
            }
        }
        repaint()
    }

    fun setDrawGrid(value: Boolean) {
        drawGrid = value;
        repaint()
    }



    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }
}
