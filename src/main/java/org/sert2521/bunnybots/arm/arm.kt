package org.sert2521.bunnybots.arm

import com.ctre.phoenix.motorcontrol.ControlMode
import org.sert2521.bunnybots.drivetrain.Talon
import org.sert2521.bunnybots.util.LEFT_ARM_MOTOR
import org.sert2521.bunnybots.util.RIGHT_ARM_MOTOR
import org.sert2521.bunnybots.util.frontSwitch
import org.sert2521.bunnybots.util.rightJoystick
import org.sert2521.bunnybots.util.tossSwitch

private val leftArmTalon = Talon(LEFT_ARM_MOTOR)

private val defaultCommand = ManualMove(leftArmTalon)

fun initArm() {
    Talon(RIGHT_ARM_MOTOR).follow(leftArmTalon)
    if (rightJoystick.getRawButtonPressed(11)) {
        Move(leftArmTalon)
        defaultCommand
    }

    leftArmTalon
    leftArmTalon.set(ControlMode.Position, 2000.0)

    if (tossSwitch.get()) {
        Toss(leftArmTalon)
        defaultCommand
    }

    if (frontSwitch.get()) {
        leftArmTalon.setSelectedSensorPosition(0, 0, 0)
    }
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
    leftArmTalon.stopMotor()
    defaultCommand
}

fun setArmAngle(angle: Double) {
    // 1409..-1171 to 3931..1350

}

fun setArmSpeed(speed: Double) {
}
