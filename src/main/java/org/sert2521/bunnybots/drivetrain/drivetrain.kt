package org.sert2521.bunnybots.drivetrain

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.drive.DifferentialDrive
import org.sert2521.bunnybots.util.LEFT_FRONT_MOTOR
import org.sert2521.bunnybots.util.LEFT_REAR_MOTOR
import org.sert2521.bunnybots.util.RIGHT_FRONT_MOTOR
import org.sert2521.bunnybots.util.RIGHT_REAR_MOTOR
import org.sert2521.bunnybots.util.leftJoystick
import org.sertain.hardware.Talon

fun Talon.autoBreak(enable: Boolean = true): Talon = apply {
    setNeutralMode(if (enable) NeutralMode.Brake else NeutralMode.Coast)
}

fun Talon.resetSensor(): Talon = apply {
    configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Int.MAX_VALUE)
    setSelectedSensorPosition(0, 0, Int.MAX_VALUE)
}

fun Talon.inverted(inverted: Boolean = true): Talon = apply { this.inverted = inverted }

object Drivetrain : Subsystem() {
    val frontLeft = Talon(LEFT_FRONT_MOTOR)
    val frontRight = Talon(RIGHT_FRONT_MOTOR).inverted()
    val rearLeft = Talon(LEFT_REAR_MOTOR)
    val rearRight = Talon(RIGHT_REAR_MOTOR).inverted()

    private val drive = DifferentialDrive(frontLeft, frontRight)

    fun setBreakMode(enable: Boolean) {
        frontLeft.autoBreak(enable)
        frontRight.autoBreak(enable)
    }

    fun arcade() {
        drive.arcadeDrive(leftJoystick.x, -leftJoystick.y)
    }

    fun tank(left: Double, right: Double) {
        drive.tankDrive(left, -right)
    }

    fun reset() {
        stop()

        frontLeft.resetSensor()
        frontRight.resetSensor()
        rearLeft.follow(frontLeft)
        rearRight.follow(frontRight)

        setBreakMode(true)
    }

    fun stop() {
        drive.stopMotor()
    }

    override fun initDefaultCommand() {
        ArcadeDrive().start()
    }
}
