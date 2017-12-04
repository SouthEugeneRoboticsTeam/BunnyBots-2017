package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.strongback.command.Command
import org.strongback.command.Requirable

abstract class TalonResetCommandBase(
        requirements: Collection<Requirable>,
        private val talon: CANTalon
) : Command(*requirements.toTypedArray()) {
    override fun end() = talon.softReset()
}
