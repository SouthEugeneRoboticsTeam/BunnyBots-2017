package org.sert2521.bunnybots.drivetrain

import edu.wpi.first.wpilibj.command.Command
import org.sert2521.bunnybots.util.leftJoystick

/**
 * This command allows for arcade drive of the robot.
 */
class ArcadeDrive : Command() {
    override fun isFinished(): Boolean = false

    override fun execute() {
        frontDrive.arcadeDrive(leftJoystick.x, leftJoystick.y)
        rearDrive.arcadeDrive(leftJoystick.x, leftJoystick.y)
    }

    override fun end() {
        frontDrive.stopMotor()
        rearDrive.stopMotor()
    }
}
