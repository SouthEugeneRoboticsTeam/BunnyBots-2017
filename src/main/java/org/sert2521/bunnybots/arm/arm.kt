package org.sert2521.bunnybots.arm

import org.sert2521.bunnybots.util.LEFT_ARM_MOTOR
import org.sert2521.bunnybots.util.RIGHT_ARM_MOTOR
import org.sert2521.bunnybots.util.onTriggeredLifecycleSubmit
import org.sert2521.bunnybots.util.rightJoystick
import org.strongback.Strongback
import org.strongback.hardware.Hardware
import java.util.function.Supplier

private val rightArmMotor = Hardware.Controllers.talonController(RIGHT_ARM_MOTOR, 0.0, 0.0)
private val leftArmMotor = Hardware.Controllers.talonController(LEFT_ARM_MOTOR, 0.0, 0.0)
private val armMotors = setOf(rightArmMotor, leftArmMotor)

fun initArm() {
    Strongback.switchReactor().onTriggeredLifecycleSubmit(rightJoystick.getButton(11), Supplier {
        Move(armMotors)
    })
}

fun setArmAngle(angle: Double) {
    rightArmMotor.withTarget(angle)
    leftArmMotor.withTarget(angle)
}
