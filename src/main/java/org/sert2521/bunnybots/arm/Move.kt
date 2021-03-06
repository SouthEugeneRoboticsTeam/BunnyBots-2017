package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.sert2521.bunnybots.util.rightJoystick
import org.sert2521.bunnybots.util.scaledThrottle
import org.strongback.control.TalonController

class Move(arm: TalonController, talon: CANTalon) : TalonResetCommandBase(talon, arm) {
    override fun execute(): Boolean {
        setArmAngle(rightJoystick.scaledThrottle.read())
        return false
    }
}
