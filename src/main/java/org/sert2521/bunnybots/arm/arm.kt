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
private val leftArmMotor = Hardware.Controllers.talonController(leftArmTalon, 11.3777777778, 0.0).apply {
    setFeedbackDevice(TalonSRX.FeedbackDevice.QUADRATURE_ENCODER)
    withGains(0.01, 0.0, 0.0)
}
private val defaultCommand = ManualMove(leftArmMotor, leftArmTalon)

fun initArm() {
    CANTalon(RIGHT_ARM_MOTOR).follow(leftArmTalon)
    Strongback.switchReactor().onTriggeredLifecycleSubmit(rightJoystick.getButton(11), Supplier {
        Move(leftArmMotor, leftArmTalon)
    }, Supplier { defaultCommand })
}

fun addArmCommands() {
//    Strongback.executor().register(
//            SoftwarePIDController(
//                    SoftwarePIDController.SourceType.DISTANCE,
//                    DoubleSupplier { leftArmTalon.pulseWidthPosition.toDouble() },
//                    DoubleConsumer { leftArmTalon.set(it) }
//            ).executable(),
//            Executor.Priority.HIGH
//    )
    leftArmTalon.softReset()
    Strongback.submit(defaultCommand)
}

fun setArmAngle(angle: Double) {
    // 1409..-1171 to 3931..1350
    Strongback.logger().info("2 " + leftArmTalon.encPosition.toString())
    Strongback.logger().info("3 " + leftArmTalon.pulseWidthPosition.toString())
    leftArmMotor.setFeedbackDevice(TalonSRX.FeedbackDevice.QUADRATURE_ENCODER)
    leftArmMotor.controlMode = TalonController.ControlMode.POSITION
    leftArmTalon.set(200.0)
//    leftArmMotor.withTarget(237.0)
}

fun setArmSpeed(speed: Double) {
    leftArmMotor.controlMode = TalonController.ControlMode.PERCENT_VBUS
    leftArmMotor.speed = speed * 0.3
}

fun CANTalon.softReset() {
    reset()
    enable()
}
