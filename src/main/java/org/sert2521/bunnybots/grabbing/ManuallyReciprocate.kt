package org.sert2521.bunnybots.grabbing

import org.strongback.command.Command
import org.strongback.components.Motor

class ManuallyReciprocate(
        private val arm: Motor
) : Command(arm) {
    override fun execute(): Boolean {
        arm.speed = 0.5 // TODO
        return false
    }

    override fun end() = arm.stop()
}