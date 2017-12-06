package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.strongback.command.Command
import org.strongback.control.TalonController
import java.util.concurrent.TimeUnit

abstract class TalonResetCommandBase(
        private val talon: CANTalon,
        requirements: TalonController,
        timeout: Long = 0
) : Command(TimeUnit.MILLISECONDS.toNanos(timeout), listOf(requirements)) {
    override fun end() = talon.softReset()
}
