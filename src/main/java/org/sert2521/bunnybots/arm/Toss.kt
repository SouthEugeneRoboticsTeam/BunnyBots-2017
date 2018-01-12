package org.sert2521.bunnybots.arm

import org.sert2521.bunnybots.drivetrain.Talon

class Toss(talon: Talon) : TalonResetCommandBase(talon, 300.0) {
    private var counter = 0

    override fun initialize() {
        setArmSpeed(-.4)
    }

    override fun isFinished(): Boolean = false

    override fun execute() {
        setArmSpeed(-when (counter++) {
            0 -> .4
            in 1..10 -> .6
            else -> .8
        })
    }
}
