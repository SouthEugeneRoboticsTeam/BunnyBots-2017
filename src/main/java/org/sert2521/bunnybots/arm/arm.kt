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

private val rightArmTalon = CANTalon(RIGHT_ARM_MOTOR)
private val rightArmMotor = Hardware.Controllers.talonController(rightArmTalon, 0.1, 0.0).apply {
    withGains(0.1, 0.1, 0.1)
    setFeedbackDevice(TalonSRX.FeedbackDevice.ANALOG_ENCODER)
    controlMode = TalonController.ControlMode.POSITION
}
private val leftArmTalon = CANTalon(LEFT_ARM_MOTOR)
private val leftArmMotor = Hardware.Controllers.talonController(leftArmTalon, 0.1, 0.0).apply {
    withGains(0.1, 0.1, 0.1)
    setFeedbackDevice(TalonSRX.FeedbackDevice.ANALOG_ENCODER)
    controlMode = TalonController.ControlMode.POSITION
}
private val armMotors = setOf(rightArmMotor, leftArmMotor)

fun initArm() {
    Strongback.switchReactor().onTriggeredLifecycleSubmit(rightJoystick.getButton(11), Supplier {
        Move(armMotors)
    })


}

fun setArmAngle(angle: Double) {
//    leftArmTalon.setpoint = angle
//    rightArmTalon.setpoint = angle
}
