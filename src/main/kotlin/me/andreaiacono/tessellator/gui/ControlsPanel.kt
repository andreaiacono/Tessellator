package me.andreaiacono.tessellator.gui

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener


class ControlsPanel(private val main: Main) : JPanel(), ActionListener, ChangeListener {
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
        sl.putConstraint(SpringLayout.WEST, resetButton, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.EAST, resetButton, -5, SpringLayout.EAST, this)
        sl.putConstraint(SpringLayout.SOUTH, resetButton, -5, SpringLayout.SOUTH, this)
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
        sl.putConstraint(SpringLayout.WEST, drawGridCheckbox, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.EAST, drawGridCheckbox, -5, SpringLayout.EAST, this)
        sl.putConstraint(SpringLayout.NORTH, drawGridCheckbox, 5, SpringLayout.NORTH, this)
        add(drawGridCheckbox)

        val drawcolorsCheckbox = JCheckBox("Draw colors", true)
        drawcolorsCheckbox.addActionListener {
            main.setDrawColors((it.source as JCheckBox).isSelected)
        }
        sl.putConstraint(SpringLayout.WEST, drawcolorsCheckbox, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.EAST, drawcolorsCheckbox, -5, SpringLayout.EAST, this)
        sl.putConstraint(SpringLayout.NORTH, drawcolorsCheckbox, 5, SpringLayout.SOUTH, drawGridCheckbox)
        add(drawcolorsCheckbox)

        val thicknessLabel = JLabel("Line Thickness: ")
        val thicknessSlider = JSlider(JSlider.HORIZONTAL, 1, 10, 4)
        thicknessSlider.name = "thickness"
        thicknessSlider.addChangeListener(this)
        thicknessSlider.paintTicks = true
        thicknessSlider.paintTrack = true
        thicknessSlider.paintLabels = true

        sl.putConstraint(SpringLayout.WEST, thicknessLabel, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, thicknessLabel, 40, SpringLayout.SOUTH, drawcolorsCheckbox)

        sl.putConstraint(SpringLayout.WEST, thicknessSlider, 2, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.EAST, thicknessSlider, -2, SpringLayout.EAST, this)
        sl.putConstraint(SpringLayout.NORTH, thicknessSlider, 50, SpringLayout.SOUTH, drawcolorsCheckbox)
        add(thicknessLabel)
        add(thicknessSlider)

        val zoomLabel = JLabel("Zoom: ")
        val zoomSlider = JSlider(JSlider.HORIZONTAL, 2, 20, 5)
        zoomSlider.name = "zoom"
        zoomSlider.addChangeListener(this)
        zoomSlider.paintTicks = true
        zoomSlider.paintTrack = true
        zoomSlider.paintLabels = true

        sl.putConstraint(SpringLayout.WEST, zoomLabel, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, zoomLabel, 40, SpringLayout.SOUTH, thicknessSlider)

        sl.putConstraint(SpringLayout.WEST, zoomSlider, 2, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.EAST, zoomSlider, -2, SpringLayout.EAST, this)
        sl.putConstraint(SpringLayout.NORTH, zoomSlider, 50, SpringLayout.SOUTH, thicknessSlider)
        add(zoomLabel)
        add(zoomSlider)
    }
}