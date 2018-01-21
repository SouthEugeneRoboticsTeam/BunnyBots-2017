package org.sert2521.bunnybots.drivetrain

import edu.wpi.first.wpilibj.DriverStation
import org.sertain.command.Command

/**
 * This command allows for arcade drive of the robot.
 */
class ArcadeDrive : Command() {
    init {
        requires(Drivetrain)
    }

    override fun execute(): Boolean {
        if (!DriverStation.getInstance().isAutonomous) Drivetrain.arcade()
        return false
    }

    override fun onDestroy() {
        Drivetrain.stop()
    }
}
