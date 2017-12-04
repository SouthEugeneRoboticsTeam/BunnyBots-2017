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

private val leftArmTalon = CANTalon(LEFT_ARM_MOTOR)
private val leftArmMotor = Hardware.Controllers.talonController(leftArmTalon, 0.1, 0.0).apply {
    withGains(0.0005, 0.0005, 0.0005)
    setFeedbackDevice(TalonSRX.FeedbackDevice.QUADRATURE_ENCODER)
    controlMode = TalonController.ControlMode.POSITION
}
private val rightArmTalon = CANTalon(RIGHT_ARM_MOTOR).apply {
    follow(leftArmTalon)
}
private val rightArmMotor = Hardware.Controllers.talonController(rightArmTalon, 0.1, 0.0)
private val armMotors = setOf(leftArmMotor, rightArmMotor)

private val defaultCommand = ManualMove(armMotors, leftArmTalon)

fun initArm() {
    leftArmTalon.softReset()
    Strongback.switchReactor().onTriggeredLifecycleSubmit(rightJoystick.getButton(11), Supplier {
        Move(armMotors, leftArmTalon)
    }, Supplier { defaultCommand })
}

fun addArmCommands() {
    Strongback.submit(defaultCommand)
}

fun setArmAngle(angle: Double) {
    // 1409..-1171 to 3931..1350
    Strongback.logger().info(leftArmTalon.encPosition.toString())
    leftArmMotor.withTarget(60.0)
}

fun setArmSpeed(speed: Double) {
    leftArmMotor.speed = speed * 0.2
}

fun CANTalon.softReset() {
    reset()
    enable()
}
