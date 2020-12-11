package me.andreaiacono.tessellator.gui

import me.andreaiacono.tessellator.core.Cell
import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class Main(title: String) : JFrame(), ActionListener {

    private val controlsPanel: ControlsPanel
    private val canvasPanel: CanvasPanel
    private val statusBar: JLabel? = null

    init {
        setSize(750, 627)
        setTitle(title)
        defaultCloseOperation = EXIT_ON_CLOSE
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
        } catch (e: Exception) {
            // just tried
        }
        controlsPanel = ControlsPanel(this)
        canvasPanel = CanvasPanel(this)
        val divider = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, canvasPanel, controlsPanel)
        divider.resizeWeight = 0.85
        add(divider, BorderLayout.CENTER)
        isVisible = true
    }

    fun resetCanvas() {
        canvasPanel.cell = Cell()
        repaint()
    }
    fun getCanvasPanel(): CanvasPanel {
        return canvasPanel
    }

    override fun actionPerformed(e: ActionEvent) {
        repaint()
    }

}

private fun createAndShowGUI() {
    val frame = Main("Tessellator")
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}

