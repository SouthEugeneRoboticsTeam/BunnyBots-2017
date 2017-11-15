package org.sert2521.bunnybots.claw

import org.strongback.command.Command
import org.strongback.components.Solenoid

class Grab(private val claw: Solenoid) : Command() {

    override fun initialize() {
        claw.extend()
    }

    override fun execute() = false

    override fun end() {
        claw.retract()
    }
}
