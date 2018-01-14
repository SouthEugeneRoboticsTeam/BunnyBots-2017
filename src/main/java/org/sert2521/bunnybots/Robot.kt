package org.sert2521.bunnybots

import com.ctre.phoenix.motorcontrol.FeedbackDevice
import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.command.Scheduler
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.followers.EncoderFollower
import jaci.pathfinder.modifiers.TankModifier
import org.opencv.core.Point
import org.sert2521.bunnybots.drivetrain.Drivetrain
import org.sertain.hardware.Talon

@Suppress("MemberVisibilityCanPrivate", "HasPlatformType")
/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Robot : IterativeRobot() {
    override fun robotInit() {
        Drivetrain
    }

    val l = Talon(14)
    val r = Talon(10)

    val points = arrayOf(
            Waypoint(-4.0, -1.0, Pathfinder.d2r(-45.0)), // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
            Waypoint(-2.0, -2.0, 0.0), // Waypoint @ x=-2, y=-2, exit angle=0 radians
            Waypoint(0.0, 0.0, 0.0),  // Waypoint @ x=0, y=0,   exit angle=0 radians
            Waypoint(1.0, 0.5, 0.0)  // Waypoint @ x=0, y=0,   exit angle=0 radians
    )

    var config = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.1, 2.356, 1.414, 60.0)
    var trajectory = Pathfinder.generate(points, config)

    val modifier = TankModifier(trajectory).modify(0.86)


    val left = EncoderFollower(modifier.leftTrajectory)
    val right = EncoderFollower(modifier.rightTrajectory)

    val output = mutableListOf<Point>()

    override fun autonomousInit() {
        left.reset()
        right.reset()

        l.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10000)
        r.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10000)

        l.setSelectedSensorPosition(0, 0, 10000)
        r.setSelectedSensorPosition(0, 0, 10000)

        r.inverted = true

        println(trajectory.segments.size)
        println(trajectory.segments.joinToString<Trajectory.Segment>("\n") { "${it.x}, ${it.y}"})

        left.configureEncoder(0, 8192, 0.15)
        left.configurePIDVA(0.8, 0.0, 0.0, 1.0 / 2.356, 0.0)

        right.configureEncoder(0, 8192, 0.15)
        right.configurePIDVA(0.8, 0.0, 0.0, 1.0 / 2.356, 0.0)
    }

    override fun autonomousPeriodic() {
        Scheduler.getInstance().run()

        val leftPosition = l.getSelectedSensorPosition(0) * -1
        val rightPosition = r.getSelectedSensorPosition(0) * -1

        println("left: $leftPosition")
        println("right: $rightPosition")

        val leftOut = left.calculate(leftPosition)
        val rightOut = right.calculate(rightPosition)

        if (left.isFinished) {
            l.stopMotor()
            r.stopMotor()
            val a = output.filterIndexed { index, _ -> index % 2 == 0 }
            println(a.mapIndexed { i, p -> Point(i.toDouble(), p.x) }.joinToString("\n") { "${it.x}, ${it.y}"})
            println(a.mapIndexed { i, p -> Point(i.toDouble(), p.y) }.joinToString("\n") { "${it.x}, ${it.y}"})
            error("")
        } else {
            println("left output: $leftOut")
            println("right output: $rightOut")
            output += Point(leftOut, rightOut)
            l.set(leftOut)
            r.set(rightOut)
        }
    }

    override fun teleopPeriodic() = Scheduler.getInstance().run()
}
