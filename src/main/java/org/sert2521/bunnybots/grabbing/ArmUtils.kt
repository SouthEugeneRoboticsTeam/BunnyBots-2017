package org.sert2521.bunnybots.grabbing

import org.sert2521.bunnybots.util.LEFT_ARM_MOTOR
import org.sert2521.bunnybots.util.RIGHT_ARM_MOTOR
import org.strongback.Strongback
import org.strongback.components.Motor
import org.strongback.hardware.Hardware

private val arm: Motor = Motor.compose(
        Hardware.Motors.talonSRX(RIGHT_ARM_MOTOR),
        Hardware.Motors.talonSRX(LEFT_ARM_MOTOR)
)

fun initArm() {
    Strongback.submit(Reciprocate(arm))
}
