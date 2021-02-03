package me.andreaiacono.tessellator.gui

import java.awt.Color
import java.awt.Color.GRAY
import java.awt.Color.WHITE
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.SpringLayout.*
import javax.swing.border.BevelBorder.RAISED
import javax.swing.border.EtchedBorder
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener


class ControlsPanel(private val main: Main) : JPanel(), ActionListener, ChangeListener {
    private val color1Label: JLabel = JLabel("  ")
    private val color2Label: JLabel = JLabel("  ")
    private var color1: Color = WHITE
    private var color2: Color = GRAY

    override fun actionPerformed(e: ActionEvent) {
        main.repaint()
    }

    override fun stateChanged(e: ChangeEvent) {
        val slider = (e.source as JSlider)
        when (slider.name) {
            "thickness" -> main.setThickness((e.source as JSlider).value)
            "zoom" -> main.setZoom((e.source as JSlider).value)
        }
    }

    init {
        val sl = SpringLayout()
        layout = sl
        val resetButton = JButton("Reset")
        resetButton.actionCommand = "reset"
        sl.putConstraint(WEST, resetButton, 5, WEST, this)
        sl.putConstraint(EAST, resetButton, -5, EAST, this)
        sl.putConstraint(SOUTH, resetButton, -5, SOUTH, this)
        resetButton.addActionListener { e: ActionEvent? ->
            if (resetButton.actionCommand == "reset") {
                main.resetCanvas()
            }
        }
        add(resetButton)

        val drawGridCheckbox = JCheckBox("Draw Grid", true)
        drawGridCheckbox.addActionListener {
            main.setDrawGrid((it.source as JCheckBox).isSelected)
        }
        sl.putConstraint(WEST, drawGridCheckbox, 5, WEST, this)
        sl.putConstraint(EAST, drawGridCheckbox, -5, EAST, this)
        sl.putConstraint(NORTH, drawGridCheckbox, 5, NORTH, this)
        add(drawGridCheckbox)

        val drawcolorsCheckbox = JCheckBox("Draw colors", true)
        drawcolorsCheckbox.addActionListener {
            main.setDrawColors((it.source as JCheckBox).isSelected)
        }
        sl.putConstraint(WEST, drawcolorsCheckbox, 5, WEST, this)
        sl.putConstraint(EAST, drawcolorsCheckbox, -5, EAST, this)
        sl.putConstraint(NORTH, drawcolorsCheckbox, 5, SOUTH, drawGridCheckbox)
        add(drawcolorsCheckbox)

        val color1Button = JButton("Color 1")
        color1Button.addActionListener {
            val selectedColor = JColorChooser.showDialog(this, "Pick a Color", color1);
            if (selectedColor != null) {
                color1 = selectedColor
                color1Label.background = color1
                main.setColor1(color1)
            }
        }
        sl.putConstraint(WEST, color1Button, 5, WEST, this)
        sl.putConstraint(EAST, color1Button, -50, EAST, this)
        sl.putConstraint(NORTH, color1Button, 5, SOUTH, drawcolorsCheckbox)
        add(color1Button)

        color1Label.background = color1
        color1Label.isOpaque = true
        color1Label.border = EtchedBorder(RAISED)
        sl.putConstraint(WEST, color1Label, 5, EAST, color1Button)
        sl.putConstraint(EAST, color1Label, -5, EAST, this)
        sl.putConstraint(NORTH, color1Label, 2, NORTH, color1Button)
        sl.putConstraint(SOUTH, color1Label, -2, SOUTH, color1Button)
        add(color1Label)

        val color2Button = JButton("Color 2");
        color2Button.addActionListener {
            val selectedColor = JColorChooser.showDialog(this, "Pick a Color", color2);
            if (selectedColor != null) {
                color2 = selectedColor
                color2Label.background = color2
                main.setColor2(color2)
            }
        }
        sl.putConstraint(WEST, color2Button, 5, WEST, this)
        sl.putConstraint(EAST, color2Button, -50, EAST, this)
        sl.putConstraint(NORTH, color2Button, 5, SOUTH, color1Button)
        add(color2Button)

        color2Label.background = color2
        color2Label.isOpaque = true
        color2Label.border = EtchedBorder(RAISED)
        sl.putConstraint(WEST, color2Label, 5, EAST, color2Button)
        sl.putConstraint(EAST, color2Label, -5, EAST, this)
        sl.putConstraint(NORTH, color2Label, 2, NORTH, color2Button)
        sl.putConstraint(SOUTH, color2Label, -2, SOUTH, color2Button)
        add(color2Label)

        val thicknessLabel = JLabel("Line Thickness: ")
        val thicknessSlider = JSlider(JSlider.HORIZONTAL, 1, 10, 4)
        thicknessSlider.name = "thickness"
        thicknessSlider.addChangeListener(this)
        thicknessSlider.paintTicks = true
        thicknessSlider.paintTrack = true
        thicknessSlider.paintLabels = true

        sl.putConstraint(WEST, thicknessLabel, 5, WEST, this)
        sl.putConstraint(NORTH, thicknessLabel, 40, SOUTH, color2Button)

        sl.putConstraint(WEST, thicknessSlider, 2, WEST, this)
        sl.putConstraint(EAST, thicknessSlider, -2, EAST, this)
        sl.putConstraint(NORTH, thicknessSlider, 50, SOUTH, color2Button)
        add(thicknessLabel)
        add(thicknessSlider)

        val zoomLabel = JLabel("Zoom: ")
        val zoomSlider = JSlider(JSlider.HORIZONTAL, 2, 20, 5)
        zoomSlider.name = "zoom"
        zoomSlider.addChangeListener(this)
        zoomSlider.paintTicks = true
        zoomSlider.paintTrack = true
        zoomSlider.paintLabels = true

        sl.putConstraint(WEST, zoomLabel, 5, WEST, this)
        sl.putConstraint(NORTH, zoomLabel, 40, SOUTH, thicknessSlider)

        sl.putConstraint(WEST, zoomSlider, 2, WEST, this)
        sl.putConstraint(EAST, zoomSlider, -2, EAST, this)
        sl.putConstraint(NORTH, zoomSlider, 50, SOUTH, thicknessSlider)
        add(zoomLabel)
        add(zoomSlider)
    }
}