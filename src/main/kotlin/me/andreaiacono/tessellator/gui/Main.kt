package me.andreaiacono.tessellator.gui

import me.andreaiacono.tessellator.core.Cell
import me.andreaiacono.tessellator.core.FileData
import me.andreaiacono.tessellator.core.deserialize
import me.andreaiacono.tessellator.core.serialize
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Cursor
import java.awt.EventQueue
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.io.File
import java.net.URL
import java.util.prefs.Preferences
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.JMenuItem
import javax.swing.JMenu
import javax.swing.JMenuBar

class Main(title: String) : JFrame(), ActionListener {

    private val PREFERENCES_LAST_USED_DIRECTORY: String = ""
    private val controlsPanel: ControlsPanel
    private val canvasPanel: CanvasPanel
    private val statusBar: JLabel? = null
    private val waitCursor = Cursor.WAIT_CURSOR
    private val defaultCursor = Cursor.DEFAULT_CURSOR

    init {
        setSize(800, 650)
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
        divider.resizeWeight = 0.80
        add(divider, BorderLayout.CENTER)
        isVisible = true

        val imageURL: URL = Main::class.java.getResource("/tessellator.png")
        iconImage = ImageIcon(imageURL, "Tessellator Icon").image

        jMenuBar = createMenus()
    }

    private fun createMenus(): JMenuBar {

        val menuBar = JMenuBar()
        val fileMenu = JMenu("File")
        fileMenu.mnemonic = KeyEvent.VK_F

        val openItem = JMenuItem("Open")
        openItem.mnemonic = KeyEvent.VK_O
        fileMenu.add(openItem)
        openItem.addActionListener {
            val fileData = openFile()
            if (fileData != null) {
                canvasPanel.cell = fileData.cell
                canvasPanel.setDrawColorsCheckbox(fileData.drawColor)
                canvasPanel.setDrawGridCheckbox(fileData.drawGrid)
                canvasPanel.setZoomValue(fileData.zoom)
                canvasPanel.setLineThickness(fileData.lineThickness)
                canvasPanel.color1 = Color(fileData.color1)
                canvasPanel.color2 = Color(fileData.color2)
            }
            repaint()
        }

        val saveItem = JMenuItem("Save")
        saveItem.mnemonic = KeyEvent.VK_S
        fileMenu.add(saveItem)
        saveItem.addActionListener {
            val fileData = FileData(
                canvasPanel.cell,
                canvasPanel.zoom,
                canvasPanel.thickness,
                canvasPanel.drawGrid,
                canvasPanel.drawColors,
                canvasPanel.color1.rgb,
                canvasPanel.color2.rgb
            )
            saveFile(fileData.serialize())
        }

        fileMenu.add(JSeparator())

        val quitItem = JMenuItem("Quit")
        quitItem.mnemonic = KeyEvent.VK_Q
        fileMenu.add(quitItem)
        menuBar.add(fileMenu)

        val toolsMenu = JMenu("Tools")
        toolsMenu.mnemonic = KeyEvent.VK_T;
        val exportItem = JMenuItem("Export as PNG")
        exportItem.mnemonic = KeyEvent.VK_E
        exportItem.addActionListener { exportAsPng() }
        toolsMenu.add(exportItem)
        menuBar.add(toolsMenu)

        return menuBar
    }

    private fun exportAsPng() {
        // sets the cursor
        setCursor(waitCursor)
        try {
            val fc = JFileChooser()
            val p = Preferences.userRoot()
            val lastUsedDirectory = p[PREFERENCES_LAST_USED_DIRECTORY, ""]
            fc.currentDirectory = File(lastUsedDirectory)
            val userResponse = fc.showSaveDialog(this@Main)

            // if the user pressed "cancel" skips all
            if (userResponse != JFileChooser.APPROVE_OPTION) {
                return
            }
            var exportFilename = fc.selectedFile.absolutePath
            if (!exportFilename.toLowerCase().endsWith(".png")) {
                exportFilename += ".png"
            }
            ImageIO.write(canvasPanel.image.getSubimage(0, 0, canvasPanel.width, canvasPanel.height), "png", File(exportFilename))
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        } finally {
            setCursor(defaultCursor)
        }
    }

    private fun saveFile(fileContent: String) {
        val fc = JFileChooser()
        val p = Preferences.userRoot()
        val lastUsedDirectory = p[PREFERENCES_LAST_USED_DIRECTORY, ""]
        fc.currentDirectory = File(lastUsedDirectory)
        val userResponse = fc.showSaveDialog(this@Main)

        // if the user pressed "ok"
        var fileName = fc.selectedFile.absolutePath
        if (userResponse == JFileChooser.APPROVE_OPTION) {
            if (!fileName.toLowerCase().endsWith(".tile")) {
                fileName += ".tile"
            }
            val f = File(fileName)
            if (f.exists()) {
                if (JOptionPane.showConfirmDialog(
                        null,
                        "The file $fileName already exists. Do you want to overwrite it?",
                        "Save Tile",
                        JOptionPane.YES_NO_OPTION
                    ) == JOptionPane.NO_OPTION
                ) {
                    return
                }
            }
        } else {
            return
        }
        File(fileName).writeText(fileContent)
    }

    private fun openFile(): FileData? {
        val preferences = Preferences.userRoot()
        val fc = JFileChooser()
        val lastUsedDirectory = preferences[PREFERENCES_LAST_USED_DIRECTORY, ""]
        fc.currentDirectory = File(lastUsedDirectory)
        if (fc.showOpenDialog(this@Main) != JFileChooser.APPROVE_OPTION) {
            return null
        }
        preferences.put(PREFERENCES_LAST_USED_DIRECTORY, fc.selectedFile.path)
        val fileContent = File(fc.selectedFile.absolutePath).readText()
        return deserialize(fileContent)
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
        canvasPanel.setDrawGridCheckbox(isSet)
    }

    fun setThickness(value: Int) {
        canvasPanel.setLineThickness(value)
    }

    fun setZoom(value: Int) {
        canvasPanel.setZoomValue(value)
    }

    fun setDrawColors(isSet: Boolean) {
        canvasPanel.setDrawColorsCheckbox(isSet)
        repaint()
    }

    fun setColor1(color1: Color) {
        canvasPanel.color1 = color1
        repaint()
    }
    fun setColor2(color2: Color) {
        canvasPanel.color2 = color2
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

