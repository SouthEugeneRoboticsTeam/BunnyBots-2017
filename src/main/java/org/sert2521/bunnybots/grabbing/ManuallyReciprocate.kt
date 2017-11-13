package org.sert2521.bunnybots.grabbing

import org.sert2521.bunnybots.util.rightJoystick
import org.strongback.command.Command
import org.strongback.control.TalonController

class ManuallyReciprocate(arm: Set<TalonController>) : Command(*arm.toTypedArray()) {
    override fun execute(): Boolean {
        setArmAngle(rightJoystick.throttle.read())
        return false
    }

    override fun end() = setArmAngle(0.0)
}
