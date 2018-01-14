package org.sert2521.bunnybots.arm

import org.sert2521.bunnybots.util.rightJoystick
import org.sertain.hardware.Talon

class Move(talon: Talon) : TalonResetCommandBase(talon) {
    override fun isFinished(): Boolean = false

    override fun execute() {
        setArmAngle(rightJoystick.throttle)
    }
}
