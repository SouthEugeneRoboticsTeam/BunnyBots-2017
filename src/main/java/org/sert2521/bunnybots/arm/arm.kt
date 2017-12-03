package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.sert2521.bunnybots.util.LEFT_ARM_MOTOR
import org.sert2521.bunnybots.util.RIGHT_ARM_MOTOR
import org.sert2521.bunnybots.util.onTriggeredLifecycleSubmit
import org.sert2521.bunnybots.util.rightJoystick
import org.strongback.Strongback
import org.strongback.components.TalonSRX
import org.strongback.control.TalonController
import org.strongback.hardware.Hardware
import java.util.function.Supplier

private val rightArmMotor = Hardware.Controllers.talonController(RIGHT_ARM_MOTOR, 0.1, 0.0).apply {
    withGains(0.1, 0.1, 0.1)
    setFeedbackDevice(TalonSRX.FeedbackDevice.ANALOG_ENCODER)
    controlMode = TalonController.ControlMode.POSITION
}
private val rightArmTalon = rightArmMotor.canTalon
private val leftArmMotor = Hardware.Controllers.talonController(LEFT_ARM_MOTOR, 0.1, 0.0).apply {
    withGains(0.1, 0.1, 0.1)
    setFeedbackDevice(TalonSRX.FeedbackDevice.ANALOG_ENCODER)
    controlMode = TalonController.ControlMode.POSITION
}
private val leftArmTalon = leftArmMotor.canTalon
private val armMotors = setOf(rightArmMotor, leftArmMotor)

fun initArm() {
    Strongback.switchReactor().onTriggeredLifecycleSubmit(rightJoystick.getButton(11), Supplier {
        Move(armMotors)
    })
}

fun setArmAngle(angle: Double) {
    leftArmTalon.setpoint = angle
    rightArmTalon.setpoint = angle
}

private val TalonController.canTalon: CANTalon
    get() = this::class.java.superclass.getDeclaredField("talon").apply {
        isAccessible = true
    }.get(this) as CANTalon
