package org.sert2521.bunnybots.drivetrain

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import org.sert2521.bunnybots.util.LEFT_FRONT_MOTOR
import org.sert2521.bunnybots.util.LEFT_REAR_MOTOR
import org.sert2521.bunnybots.util.RIGHT_FRONT_MOTOR
import org.sert2521.bunnybots.util.RIGHT_REAR_MOTOR
import org.sertain.hardware.Talon

operator fun Talon.plus(other: Talon): Talon {
    other.set(ControlMode.Follower, deviceID.toDouble())
    return this
}

private val frontLeft = Talon(LEFT_FRONT_MOTOR).apply {
    setNeutralMode(NeutralMode.Brake)
}
private val frontRight = Talon(RIGHT_FRONT_MOTOR).apply {
    setNeutralMode(NeutralMode.Brake)
}
private val rearLeft = Talon(LEFT_REAR_MOTOR).apply {
    setNeutralMode(NeutralMode.Brake)
}
private val rearRight = Talon(RIGHT_REAR_MOTOR).apply {
    setNeutralMode(NeutralMode.Brake)
}

val frontDrive = DifferentialDrive(frontLeft, frontRight)
val rearDrive = DifferentialDrive(rearLeft, rearRight)

private val defaultDrive: Command
    get() = ArcadeDrive()

fun initDrivetrain() {
}

fun addDrivetrainCommands() {
    defaultDrive.start()
}
