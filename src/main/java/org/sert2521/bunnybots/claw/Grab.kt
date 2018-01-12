package org.sert2521.bunnybots.claw

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Command

class Grab(private val claw: DoubleSolenoid) : Command() {
    override fun initialize() {
        println("Extending")
        claw.set(DoubleSolenoid.Value.kForward)
    }

    override fun isFinished() = false

    override fun end() {
        println("Retracting")
        claw.set(DoubleSolenoid.Value.kReverse)
    }
}
