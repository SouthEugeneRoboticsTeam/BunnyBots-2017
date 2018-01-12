package org.sert2521.bunnybots.arm

import org.sert2521.bunnybots.drivetrain.Talon
import org.sert2521.bunnybots.util.leftJoystick

class ManualMove(talon: Talon) : TalonResetCommandBase(talon) {
    override fun isFinished(): Boolean = false

    override fun execute() {
        setArmSpeed(-leftJoystick.y)
    }
}
