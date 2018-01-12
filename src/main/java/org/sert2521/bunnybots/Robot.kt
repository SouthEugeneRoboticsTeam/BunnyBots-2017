package org.sert2521.bunnybots

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.followers.EncoderFollower
import jaci.pathfinder.modifiers.TankModifier
import org.sert2521.bunnybots.arm.addArmCommands
import org.sert2521.bunnybots.drivetrain.addDrivetrainCommands

/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Robot : IterativeRobot() {
    override fun robotInit() {
        println("Robot starting...")
//        initDrivetrain()
//        initClaw()
//        initArm()
    }

    override fun autonomousInit() {
        println("Autonomous starting...")
//        claw.set(DoubleSolenoid.Value.kForward)
//        addDefaultCommands()

        val points = arrayOf(Waypoint(-4.0, -1.0, Pathfinder.d2r(-45.0)), // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
                Waypoint(-2.0, -2.0, 0.0), // Waypoint @ x=-2, y=-2, exit angle=0 radians
                Waypoint(0.0, 0.0, 0.0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
        )

        val config = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0)
        val trajectory = Pathfinder.generate(points, config)

        val modifier = TankModifier(trajectory).modify(0.5)

        val left = EncoderFollower(modifier.leftTrajectory)
        val right = EncoderFollower(modifier.rightTrajectory)

        left.configureEncoder(0, 1000, 0.1)
        left.configurePIDVA(1.0, 0.0, 0.0, 1.0/5.0, 0.0)

        val output = left.calculate(0)

        println(output)
    }

    override fun teleopInit() {
        println("Teleop starting...")
//        claw.set(DoubleSolenoid.Value.kReverse)
//        addDefaultCommands()
    }

    override fun testPeriodic() = LiveWindow.run()

//    override fun disabledInit() = Strongback.disable()

    private fun addDefaultCommands() {
        JoystickButton(Joystick(0), 0).whenActive(object : Command() {
            override fun isFinished(): Boolean {
                println("Bucket!")
                return true
            }
        })
        addDrivetrainCommands()
        addArmCommands()
    }
}
