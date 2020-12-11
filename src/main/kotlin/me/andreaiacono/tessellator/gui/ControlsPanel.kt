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
        val distanceLabel = JLabel("Distance: ")
        sl.putConstraint(SpringLayout.WEST, distanceLabel, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, distanceLabel, 5, SpringLayout.NORTH, this)
        val euclideanRadioButton = JRadioButton("Euclidean")
        euclideanRadioButton.isSelected = true
        euclideanRadioButton.addActionListener(this)
        sl.putConstraint(SpringLayout.WEST, euclideanRadioButton, 15, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, euclideanRadioButton, 5, SpringLayout.SOUTH, distanceLabel)
        val chebyshevRadioButton = JRadioButton("Chebyshev")
        chebyshevRadioButton.addActionListener(this)
        sl.putConstraint(SpringLayout.WEST, chebyshevRadioButton, 15, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, chebyshevRadioButton, 5, SpringLayout.SOUTH, euclideanRadioButton)
        val canberraRadioButton = JRadioButton("Canberra")
        canberraRadioButton.addActionListener(this)
        sl.putConstraint(SpringLayout.WEST, canberraRadioButton, 15, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, canberraRadioButton, 5, SpringLayout.SOUTH, chebyshevRadioButton)
        val cosineRadioButton = JRadioButton("Cosine")
        cosineRadioButton.addActionListener(this)
        sl.putConstraint(SpringLayout.WEST, cosineRadioButton, 15, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, cosineRadioButton, 5, SpringLayout.SOUTH, canberraRadioButton)
        val manhattanRadioButton = JRadioButton("Manhattan")
        manhattanRadioButton.addActionListener(this)
        sl.putConstraint(SpringLayout.WEST, manhattanRadioButton, 15, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, manhattanRadioButton, 5, SpringLayout.SOUTH, cosineRadioButton)
        val distanceGroup = ButtonGroup()
        distanceGroup.add(cosineRadioButton)
        distanceGroup.add(canberraRadioButton)
        distanceGroup.add(euclideanRadioButton)
        distanceGroup.add(chebyshevRadioButton)
        distanceGroup.add(manhattanRadioButton)
        val sitesLabel = JLabel("Sites: ")
        val sitesSlider = JSlider(JSlider.HORIZONTAL, 2, 20, 10 )
        sitesSlider.name = "sites"
        sitesSlider.addChangeListener(this)
        sitesSlider.paintTicks = true
        sitesSlider.paintLabels = true
        sl.putConstraint(SpringLayout.WEST, sitesLabel, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.NORTH, sitesLabel, 40, SpringLayout.SOUTH, manhattanRadioButton)
        sl.putConstraint(SpringLayout.WEST, sitesSlider, 0, SpringLayout.EAST, sitesLabel)
        sl.putConstraint(SpringLayout.EAST, sitesSlider, -5, SpringLayout.EAST, this)
        sl.putConstraint(SpringLayout.NORTH, sitesSlider, 32, SpringLayout.SOUTH, manhattanRadioButton)
        val animationButton = JButton("Start animation")
        animationButton.actionCommand = "start"
        sl.putConstraint(SpringLayout.WEST, animationButton, 5, SpringLayout.WEST, this)
        sl.putConstraint(SpringLayout.EAST, animationButton, -5, SpringLayout.EAST, this)
        sl.putConstraint(SpringLayout.SOUTH, animationButton, -5, SpringLayout.SOUTH, this)
        animationButton.addActionListener { e: ActionEvent? ->
            if (animationButton.actionCommand == "start") {
                animationButton.actionCommand = "stop"
                animationButton.text = "Stop Animation"
            } else {
                animationButton.actionCommand = "start"
                animationButton.text = "Start Animation"
            }
        }
        add(cosineRadioButton)
        add(animationButton)
        add(distanceLabel)
        add(canberraRadioButton)
        add(euclideanRadioButton)
        add(chebyshevRadioButton)
        add(manhattanRadioButton)
        add(sitesLabel)
        add(sitesSlider)
    }
}