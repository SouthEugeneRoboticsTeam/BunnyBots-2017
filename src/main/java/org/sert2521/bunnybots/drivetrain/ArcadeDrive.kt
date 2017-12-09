package org.sert2521.bunnybots.drivetrain

import edu.wpi.first.wpilibj.Preferences
import org.strongback.command.Command
import org.strongback.components.ui.ContinuousRange
import org.strongback.drive.TankDrive

/**
 * This command allows for arcade drive of the robot.
 */
class ArcadeDrive(
        private val drive: TankDrive,
        private val pitch: ContinuousRange,
        private val roll: ContinuousRange
) : Command(drive) {
    private val scaling = Preferences.getInstance().getDouble("scaled_drive_speed", 1.0)

    override fun execute(): Boolean {
        drive.arcade(pitch.scale(scaling).read(), roll.scale(scaling).read())
        return false
    }

    override fun end() = drive.stop()
}
