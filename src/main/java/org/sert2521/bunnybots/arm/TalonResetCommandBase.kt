package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.strongback.command.Command
import org.strongback.control.TalonController

abstract class TalonResetCommandBase(
        requirements: TalonController,
        private val talon: CANTalon
) : Command(requirements) {
    override fun end() = talon.softReset()
}
