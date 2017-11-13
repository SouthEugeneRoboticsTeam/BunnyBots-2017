package org.sert2521.bunnybots.grabbing

import org.sert2521.bunnybots.util.LEFT_ARM_MOTOR
import org.sert2521.bunnybots.util.RIGHT_ARM_MOTOR
import org.sert2521.bunnybots.util.onTriggeredLifecycleSubmit
import org.sert2521.bunnybots.util.rightJoystick
import org.strongback.Strongback
import org.strongback.components.Motor
import org.strongback.hardware.Hardware
import java.util.function.Supplier

private val arm: Motor = Motor.compose(
        Hardware.Motors.talonSRX(RIGHT_ARM_MOTOR),
        Hardware.Motors.talonSRX(LEFT_ARM_MOTOR)
)

fun initArm() {
    Strongback.submit(Reciprocate(arm))
    Strongback.switchReactor().onTriggeredLifecycleSubmit(rightJoystick.getButton(11), Supplier {
        ManuallyReciprocate(arm)
    })
}
