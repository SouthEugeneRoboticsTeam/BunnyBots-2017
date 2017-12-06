package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.sert2521.bunnybots.util.leftJoystick
import org.strongback.control.TalonController

class ManualMove(arm: TalonController, talon: CANTalon) : TalonResetCommandBase(talon, arm) {
    override fun execute(): Boolean {
        setArmSpeed(-leftJoystick.pitch.scale(0.3).read())
        return false
    }
}
