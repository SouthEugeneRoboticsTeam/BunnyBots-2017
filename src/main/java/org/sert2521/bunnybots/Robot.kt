package org.sert2521.bunnybots

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.command.Command
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.followers.EncoderFollower
import jaci.pathfinder.modifiers.TankModifier
import org.sert2521.bunnybots.arm.addArmCommands
import org.sert2521.bunnybots.drivetrain.addDrivetrainCommands
import org.sertain.hardware.Talon

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

    val points = arrayOf(
            Waypoint(-4.0, -2.0, Pathfinder.d2r(89.0)),
            Waypoint(-2.0, -2.0, 0.0),
            Waypoint(0.0, 0.0, 0.0)
    )

    val config = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 3.75, 2.0, 60.0)
    val trajectory = Pathfinder.generate(points, config)

    val modifier = TankModifier(trajectory).modify(0.86)

    val left = EncoderFollower(modifier.leftTrajectory)
    val right = EncoderFollower(modifier.rightTrajectory)

    val l = Talon(14)
    val r = Talon(10)

    var leftInitial = 0
    var rightInitial = 0

    override fun autonomousInit() {
        left.reset()
        right.reset()

        l.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000)
        r.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 1000)

        l.setSelectedSensorPosition(0, 0, 10000)
        r.setSelectedSensorPosition(0, 0, 10000)

        r.inverted = true

        println(trajectory.segments.contentDeepToString())
        println(trajectory.segments.size)

        leftInitial = l.getSelectedSensorPosition(0) * -1
        rightInitial = r.getSelectedSensorPosition(0) * -1

        println(leftInitial)
        println(rightInitial)

        left.configureEncoder(leftInitial, 8192, 0.15)
        left.configurePIDVA(0.8, 0.0, 0.0, 1.0/3.75, 0.0)

        right.configureEncoder(rightInitial, 8192, 0.15)
        right.configurePIDVA(0.8, 0.0, 0.0, 1.0/3.75, 0.0)
    }

    override fun autonomousPeriodic() {
        val leftPosition = l.getSelectedSensorPosition(0) * -1
        val rightPosition = r.getSelectedSensorPosition(0) * -1

//        println("left: $leftPosition")
//        println("right: $rightPosition")

//        val leftOut = left.calculate(leftPosition - leftInitial)
//        val rightOut = right.calculate(rightPosition - rightInitial)
//
//        if (left.isFinished) {
//            l.stopMotor()
//            r.stopMotor()
//        } else {
//            println("left output: $leftOut")
//            println("right output: $rightOut")
//            l.set(leftOut)
//            r.set(rightOut)
//        }
    }

    override fun teleopInit() {
        println("Teleop starting...")

        l.stopMotor()
        r.stopMotor()
//        claw.set(DoubleSolenoid.Value.kReverse)
//        addDefaultCommands()
    }

    override fun disabledInit() {
        l.stopMotor()
        r.stopMotor()
    }

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
