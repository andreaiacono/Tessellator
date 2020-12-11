package me.andreaiacono.tessellator.gui

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener


class ControlsPanel(private val main: Main) : JPanel(), ActionListener, ChangeListener {
    override fun actionPerformed(e: ActionEvent) {
        val actionCommand = e.actionCommand
        main.repaint()
    }

    override fun stateChanged(e: ChangeEvent) {
        val n = (e.source as JSlider).value
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
    }
}