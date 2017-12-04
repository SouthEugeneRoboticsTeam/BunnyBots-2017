package org.sert2521.bunnybots

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import org.sert2521.bunnybots.arm.addArmCommands
import org.sert2521.bunnybots.arm.initArm
import org.sert2521.bunnybots.claw.initClaw
import org.sert2521.bunnybots.drivetrain.addDrivetrainCommands
import org.sert2521.bunnybots.drivetrain.initDrivetrain
import org.strongback.Strongback

/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Robot : IterativeRobot() {
    override fun robotInit() {
        Strongback.logger().info("Robot starting...")
        Strongback.start()
        initDrivetrain()
        initClaw()
        initArm()
    }

    override fun autonomousInit() {
        Strongback.logger().info("Autonomous starting...")
        Strongback.start()
        addDefaultCommands()
    }

    override fun teleopInit() {
        Strongback.logger().info("Teleop starting...")
        Strongback.start()
        addDefaultCommands()
    }

    override fun testPeriodic() = LiveWindow.run()

    override fun disabledInit() = Strongback.disable()

    private fun addDefaultCommands() {
        addDrivetrainCommands()
        addArmCommands()
    }
}
