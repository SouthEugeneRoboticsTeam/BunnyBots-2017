package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.sert2521.bunnybots.util.leftJoystick
import org.strongback.control.TalonController

class ManualMove(arm: Set<TalonController>, talon: CANTalon) : TalonResetCommandBase(arm, talon) {
    override fun execute(): Boolean {
        setArmSpeed(-leftJoystick.pitch.read())
        return false
    }
}
