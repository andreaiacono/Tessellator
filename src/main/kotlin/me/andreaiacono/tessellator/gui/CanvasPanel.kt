package me.andreaiacono.tessellator.gui

import me.andreaiacono.tessellator.core.*
import me.andreaiacono.tessellator.core.Point
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.border.BevelBorder

import java.awt.*
import java.awt.Color.*
import java.awt.event.*
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import kotlin.math.min
import java.util.LinkedList

import java.util.Queue


class CanvasPanel(private val main: Main) : JPanel(), MouseListener, ActionListener, MouseMotionListener {

    private var drawColors: Boolean = true
    private var thickness: Int = 4
    private var currentX: Int = 0
    private var currentY: Int = 0
    private var drawGrid: Boolean = true
    private var isOnDrawing: Boolean = false
    private var zoom = 5
    private var startingingCoords: Coords? = null
    private var changingPoint: Point? = null
    private var hoveringPoint: Point? = null
    private var hoveringPixel: Point? = null
    var cell = Cell()
    private val image = BufferedImage(4000, 4000, TYPE_INT_RGB)
    private val pmMenu = JPopupMenu()

    init {
        border = BorderFactory.createBevelBorder(BevelBorder.LOWERED)
        addMouseListener(this)
        addMouseMotionListener(this)


        // creates the popup menu

        // creates the popup menu
        val deletePointMenuItem = JMenuItem("Delete Point")
        deletePointMenuItem.addActionListener(this)
        pmMenu.add(deletePointMenuItem)
    }

    fun reset() {
        cell = Cell()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val imageGraphics = image.createGraphics()
        imageGraphics.color = WHITE
        imageGraphics.fillRect(0, 0, width, height)
        drawCells(imageGraphics as Graphics2D, size.width, size.height, cell)
        g.drawImage(image, 0, 0, null)
    }

    private fun drawCells(g: Graphics2D, canvasWidth: Int, canvasHeight: Int, cell: Cell) {
        val boxWidth = canvasWidth / zoom
        cell.setNewSize(boxWidth)

        // draws the grid
        if (drawGrid) {
            (0..zoom).forEach { x ->
                (0..zoom).forEach { y ->
                    g.stroke = BasicStroke(1.0f)
                    g.color = LIGHT_GRAY
                    g.drawRect(x * boxWidth, y * boxWidth, width, width)
                }
            }
        }

        // draws the lines
        (0..zoom).forEach { x ->
            (0..zoom).forEach { y ->
                drawCell(g, cell, boxWidth, x * boxWidth, y * boxWidth)
            }
        }

        // colors the shapes
        if (drawColors) {
            (0..zoom).forEach { x ->
                (0..zoom).forEach { y ->
                    if ((x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1) ) {
                        floodFill(g, ScaledPoint((x) * boxWidth - boxWidth / 2, y * boxWidth + boxWidth / 2), boxWidth)
                    }
                }
            }
        }
    }

    private fun drawCell(g: Graphics2D, cell: Cell, width: Int, left: Int, top: Int) {
        val boxHeight = width

        // draws the cell
        g.stroke = BasicStroke(thickness.toFloat())
        g.color = BLACK
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
        if (hoveringPoint != null) {
            val scaledPoint = hoveringPoint!!.scale(width, boxHeight)
            g.color = GREEN
            g.stroke = BasicStroke(2.0f)
            val circleSize = min(width / 5, 20)
            val halfCircleSize = circleSize / 2
            g.drawArc(left + scaledPoint.x - halfCircleSize, top - scaledPoint.y - halfCircleSize, circleSize, circleSize, 0, 360)
        }
        else if (hoveringPixel != null) {
            g.color = RED
            val rectSize = 5
            val scaledPoint = hoveringPixel!!.scale(width, boxHeight)
            g.fillRect(left + scaledPoint.x - 2, top - scaledPoint.y - 2, rectSize, rectSize)
        }

    }

    private fun floodFill(g: Graphics2D, point: ScaledPoint, boxWidth: Int) {
        val queue: Queue<ScaledPoint> = LinkedList()
        val coloured: MutableSet<ScaledPoint> = mutableSetOf()
        queue.add(point)
        coloured.add(point)
        g.stroke = BasicStroke(1.0f)
        g.color = GRAY
        val maxPixels = boxWidth * boxWidth * 2
        while (queue.size > 0) {
            val current: ScaledPoint = queue.poll()
            g.drawLine(current.x, current.y, current.x, current.y)
            if (coloured.size > maxPixels) {
                return
            }
            Neighbours.values().forEach { coords ->
                val neighbour = ScaledPoint(current.x + coords.x, current.y + coords.y)

                if (neighbour !in coloured &&
                    neighbour.y >= 0 && neighbour.y < height &&
                    neighbour.x >= 0 && neighbour.x < width
                ) {
                    val color = Color(image.getRGB(neighbour.x, neighbour.y))
                    if (color == WHITE || color == LIGHT_GRAY) {
                        coloured.add(neighbour)
                        queue.add(neighbour)
                    }
                }
            }
        }
    }

    override fun mouseMoved(e: MouseEvent?) {
        val current = e!!.toCoords()
        val size = width / zoom
        val currentPoint = Point(current, size)
        val existingPoint = cell.findExistingPoint(currentPoint)
        if (existingPoint != null) {
            hoveringPoint = existingPoint
            cursor = Cursor(Cursor.MOVE_CURSOR)
            isOnDrawing = true
        } else {
            hoveringPoint = null
            if (cell.findNewPointIndex(currentPoint) != null) {
                cursor = Cursor(Cursor.HAND_CURSOR)
                hoveringPixel = currentPoint
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
        if (startingingCoords != null) {
            changingPoint?.updatePosition(current, startingingCoords!!, size)
            repaint()
        }
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
            startingingCoords = current
            isOnDrawing = false
        }
    }

    fun setDrawGrid(value: Boolean) {
        drawGrid = value
        repaint()
    }

    fun setDrawColors(value: Boolean) {
        drawColors = value
        repaint()
    }

    override fun mouseReleased(e: MouseEvent?) {
        startingingCoords = null
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (hoveringPoint != null) {
            pmMenu.show(e!!.component, e.x, e.y)
        }
    }

    fun setThickness(value: Int) {
        thickness = value
        repaint()
    }

    fun setZoom(value: Int) {
        zoom = value
        repaint()
    }

    override fun actionPerformed(e: ActionEvent?) {
        cell.delete(hoveringPoint!!)
        repaint()
    }
}

enum class Neighbours(val x: Int, val y: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0)
}