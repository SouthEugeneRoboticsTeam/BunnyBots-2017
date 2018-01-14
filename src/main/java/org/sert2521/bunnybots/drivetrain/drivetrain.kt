package org.sert2521.bunnybots.drivetrain

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

object Drivetrain : Subsystem() {
    private val frontLeft = Talon(LEFT_FRONT_MOTOR).autoBreak()
    private val frontRight = Talon(RIGHT_FRONT_MOTOR).autoBreak()
    private val rearLeft = Talon(LEFT_REAR_MOTOR).autoBreak()
    private val rearRight = Talon(RIGHT_REAR_MOTOR).autoBreak()

    private val frontDrive = DifferentialDrive(frontLeft, frontRight)
    private val rearDrive = DifferentialDrive(rearLeft, rearRight)

    fun arcade() {
        frontDrive.arcadeDrive(-leftJoystick.y, leftJoystick.x)
        rearDrive.arcadeDrive(-leftJoystick.y, leftJoystick.x)
    }

    fun stop() {
        frontDrive.stopMotor()
        rearDrive.stopMotor()
    }

    override fun initDefaultCommand() {
        ArcadeDrive().start()
    }
}
