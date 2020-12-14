package me.andreaiacono.tessellator.gui

import me.andreaiacono.tessellator.core.Cell
import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.net.URL
import javax.swing.*
import javax.swing.JMenuItem
import javax.swing.JMenu
import javax.swing.JMenuBar

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

        val imageURL: URL = Main::class.java.getResource("/tessellator.png")
        iconImage = ImageIcon(imageURL, "Tessellator Icon").image

        jMenuBar = createMenus()
    }

    private fun createMenus() : JMenuBar {

        val menuBar = JMenuBar()
        val fileMenu = JMenu("File")
        fileMenu.mnemonic = KeyEvent.VK_F

        val openItem = JMenuItem("Open")
        openItem.mnemonic = KeyEvent.VK_O
        fileMenu.add(openItem)

        val saveItem = JMenuItem("Save")
        saveItem.mnemonic = KeyEvent.VK_S
        fileMenu.add(saveItem)
        fileMenu.add(JSeparator())
        val quitItem = JMenuItem("Quit")
        quitItem.mnemonic = KeyEvent.VK_Q
        quitItem.addActionListener {
            this.dispose()
        }
        fileMenu.add(quitItem)
        menuBar.add(fileMenu)

        val toolsMenu = JMenu("Tools")
        toolsMenu.mnemonic = KeyEvent.VK_T;
        val exportItem = JMenuItem("Export as PNG")
        exportItem.mnemonic = KeyEvent.VK_E
        toolsMenu.add(exportItem)
        menuBar.add(toolsMenu)

        return menuBar
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

    fun setDrawGrid(isSet: Boolean) {
        canvasPanel.setDrawGrid(isSet)
    }

    fun setThickness(value: Int) {
        canvasPanel.setThickness(value)
    }

    fun setZoom(value: Int) {
        canvasPanel.setZoom(value)
    }

    fun setDrawColors(isSet: Boolean) {
        canvasPanel.setDrawColors(isSet)
    }
}

private fun createAndShowGUI() {
    val frame = Main("Tessellator")
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}

