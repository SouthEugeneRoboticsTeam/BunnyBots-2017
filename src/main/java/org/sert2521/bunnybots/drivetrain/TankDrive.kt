package org.sert2521.bunnybots.drivetrain

import org.strongback.command.Command
import org.strongback.components.ui.ContinuousRange
import org.strongback.drive.TankDrive

/**
 * This command allows for tank drive of the robot.
 */
class TankDrive(
        private val drive: TankDrive,
        private val left: ContinuousRange,
        private val right: ContinuousRange
) : Command(drive) {
    override fun execute(): Boolean {
        drive.tank(left.read(), right.read())
        return false
    }

    override fun end() = drive.stop()
}
