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

fun Talon.autoBreak(): Talon = apply { setNeutralMode(NeutralMode.Brake) }

fun Talon.resetSensor(): Talon = apply {
    configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, Int.MAX_VALUE)
    setSelectedSensorPosition(0, 0, Int.MAX_VALUE)
}

fun Talon.inverted(inverted: Boolean = true): Talon = apply { this.inverted = inverted }

object Drivetrain : Subsystem() {
    val frontLeft = Talon(LEFT_FRONT_MOTOR).autoBreak()
    val frontRight = Talon(RIGHT_FRONT_MOTOR).autoBreak().inverted()
    val rearLeft = Talon(LEFT_REAR_MOTOR).autoBreak()
    val rearRight = Talon(RIGHT_REAR_MOTOR).autoBreak().inverted()

    private val frontDrive = DifferentialDrive(frontLeft, frontRight)
    private val rearDrive = DifferentialDrive(rearLeft, rearRight)

    fun arcade() {
        frontDrive.arcadeDrive(leftJoystick.x, -leftJoystick.y)
        rearDrive.arcadeDrive(leftJoystick.x, -leftJoystick.y)
    }

    fun tank(left: Double, right: Double) {
        frontDrive.tankDrive(left, right)
        rearDrive.tankDrive(left, right)
    }

    fun reset() {
        stop()

        frontLeft.resetSensor()
        frontRight.resetSensor()
        rearLeft.resetSensor()
        rearRight.resetSensor()
    }

    fun stop() {
        frontDrive.stopMotor()
        rearDrive.stopMotor()
    }

    override fun initDefaultCommand() {
        ArcadeDrive().start()
    }
}
