package org.sert2521.bunnybots

import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.command.Scheduler
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.followers.EncoderFollower
import jaci.pathfinder.modifiers.TankModifier
import org.sert2521.bunnybots.drivetrain.Drivetrain
import kotlin.math.roundToInt

@Suppress("MemberVisibilityCanPrivate", "HasPlatformType")
/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Robot : IterativeRobot() {
    override fun robotInit() {
        Drivetrain
    }

    val points = arrayOf(
            Waypoint(-4.0, -1.0, Pathfinder.d2r(-45.0)), // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
            Waypoint(-2.0, -2.0, 0.0), // Waypoint @ x=-2, y=-2, exit angle=0 radians
            Waypoint(0.0, 0.0, 0.0)  // Waypoint @ x=0, y=0,   exit angle=0 radians
    )

    var config = TrajectoryConfig(2.356, 1.414, 60.0)
    var trajectory = config.generate(points)

    val modifier = TankModifier(trajectory).modify(0.86)

    val left = EncoderFollower(modifier.leftTrajectory)
    val right = EncoderFollower(modifier.rightTrajectory)

    override fun autonomousInit() {
        Drivetrain.reset()
        left.reset()
        right.reset()

        println(trajectory.segments.size)
        println(trajectory.segments.joinToString<Trajectory.Segment>("\n") { it.heading.toString() })

        left.configureEncoder(0, 8192, 0.15)
        left.configurePIDVA(0.8, 0.0, 0.0, 1.0 / 2.356, 0.0)

        right.configureEncoder(0, 8192, 0.15)
        right.configurePIDVA(0.8, 0.0, 0.0, 1.0 / 2.356, 0.0)
    }

    override fun autonomousPeriodic() {
        Scheduler.getInstance().run()

        val rearLeftPosition = Drivetrain.rearLeft.getSelectedSensorPosition(0) * -1
        val rearRightPosition = Drivetrain.rearRight.getSelectedSensorPosition(0) * -1
        val frontLeftPosition = Drivetrain.frontLeft.getSelectedSensorPosition(0) * -1
        val frontRightPosition = Drivetrain.frontRight.getSelectedSensorPosition(0) * -1

        val leftOut = left.calculate(listOf(rearLeftPosition, frontLeftPosition).average().roundToInt())
        val rightOut = left.calculate(listOf(rearRightPosition, frontRightPosition).average().roundToInt())

        if (left.isFinished) {
            error("")
        } else {
            println("left output: $leftOut")
            println("right output: $rightOut")

            Drivetrain.tank(.5, .5)
        }
    }

    override fun teleopInit() {
        Drivetrain.reset()
    }

    override fun teleopPeriodic() = Scheduler.getInstance().run()
}

@JvmOverloads
fun TrajectoryConfig(
        maxVelocity: Double,
        maxAccel: Double,
        maxJerk: Double,
        fit: Trajectory.FitMethod = Trajectory.FitMethod.HERMITE_CUBIC,
        samples: Int = Trajectory.Config.SAMPLES_HIGH,
        dt: Double = 0.05
): Trajectory.Config = Trajectory.Config(
        fit,
        samples,
        dt,
        maxVelocity,
        maxAccel,
        maxJerk
)

fun Trajectory.Config.generate(points: Array<out Waypoint>): Trajectory = Pathfinder.generate(points, this)
