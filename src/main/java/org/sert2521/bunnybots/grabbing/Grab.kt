package org.sert2521.bunnybots.grabbing

import edu.wpi.first.smartdashboard.gui.elements.Command

class Grab(
        private val arm: Arm
) : Command() {
    override fun execute(): Boolean {
        grab(trigger.read())
        return false
    }

    override fun end() = grab.stop()
}