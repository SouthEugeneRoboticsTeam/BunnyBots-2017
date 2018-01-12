package org.sert2521.bunnybots.drivetrain

import edu.wpi.first.wpilibj.command.Command
import org.sert2521.bunnybots.util.leftJoystick
import org.sert2521.bunnybots.util.rightJoystick

/**
 * This command allows for arcade drive of the robot.
 */
class TankDrive : Command() {
    override fun isFinished(): Boolean = false

    override fun execute() {
        frontDrive.tankDrive(leftJoystick.y, rightJoystick.y)
        rearDrive.tankDrive(leftJoystick.y, rightJoystick.y)
    }

    override fun end() {
        frontDrive.stopMotor()
        rearDrive.stopMotor()
    }
}
