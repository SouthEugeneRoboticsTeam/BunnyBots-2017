package org.sert2521.bunnybots.grabbing

import org.strongback.command.Command
import org.strongback.components.Motor
import org.strongback.components.Switch

class Reciprocate(
        private val arm: Motor
) : Command(arm) {
    override fun execute(): Boolean {
        arm.speed = 0.5 // TODO
        return false
    }

    override fun end() = arm.stop()
}