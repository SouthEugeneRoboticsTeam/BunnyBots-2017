package org.sert2521.bunnybots

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.IterativeRobot
import edu.wpi.first.wpilibj.command.Scheduler
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint
import jaci.pathfinder.followers.EncoderFollower
import jaci.pathfinder.modifiers.TankModifier
import org.opencv.core.Point
import org.sert2521.bunnybots.drivetrain.Drivetrain
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

@Suppress("MemberVisibilityCanPrivate", "HasPlatformType")
/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Robot : IterativeRobot() {
    private var breakModeUpdateTask: TimerTask? = null

    override fun robotInit() {
        Drivetrain
    }

    val points = arrayOf(
            Waypoint(-4.0, 3.0, 0.0),
            Waypoint(-0.5, 3.0, Pathfinder.d2r(-45.0)),
            Waypoint(0.0, 0.0, Pathfinder.d2r(-90.0))
    )

    var config = TrajectoryConfig(MAX_VELOCITY, 0.1, 60.0)
    var trajectory = config.generate(points)

    val modifier = TankModifier(trajectory).modify(0.86)

    val left = EncoderFollower(modifier.leftTrajectory)
    val right = EncoderFollower(modifier.rightTrajectory)

    val ahrs = AHRS(I2C.Port.kMXP)

    override fun autonomousInit() {
        Drivetrain.reset()
        breakModeUpdateTask?.cancel()
        left.reset()
        right.reset()

        ahrs.reset()

        left.configureEncoder(0, 8192, 0.15)
        left.configurePIDVA(2.75, 0.0, 0.125, 1 / MAX_VELOCITY, 1.885)

        right.configureEncoder(0, 8192, 0.15)
        right.configurePIDVA(2.75, 0.0, 0.125, 1 / MAX_VELOCITY, 1.885)

        println(trajectory.segments.size)
        println(trajectory.segments.map { Point(it.x, it.y) }
                .filterIndexed { i, _ -> i % 2 == 0 }
                .filterIndexed { i, _ -> i % 2 == 0 }
                .filterIndexed { i, _ -> i % 2 == 0 }
                .joinToString("\n") { "${it.x}, ${it.y}" })
    }

    override fun autonomousPeriodic() {
        Scheduler.getInstance().run()

        val frontLeftPosition = Drivetrain.frontLeft.getSelectedSensorPosition(0) * -1
        val frontRightPosition = Drivetrain.frontRight.getSelectedSensorPosition(0) * -1

        val leftOut = left.calculate(frontLeftPosition)
        val rightOut = right.calculate(frontRightPosition)

        if (left.isFinished) {
            Drivetrain.tank(0.0, 0.0)
        } else {
            val angleDiff =
                    Pathfinder.boundHalfDegrees(Pathfinder.r2d(left.heading) - ahrs.angle)
            val turn = 0.0001 * angleDiff

            println("left: $leftOut, right: $rightOut, turn: $turn, left output: ${leftOut - turn}," +
                    " right output: ${rightOut + turn}")
            Drivetrain.tank(leftOut - turn, rightOut + turn)
        }
    }

    override fun teleopInit() {
        Drivetrain.reset()
        breakModeUpdateTask?.cancel()
    }

    override fun teleopPeriodic() = Scheduler.getInstance().run()

    override fun disabledInit() {
        breakModeUpdateTask = Timer().schedule(TimeUnit.SECONDS.toMillis(5)) {
            Drivetrain.setBreakMode(false)
        }
    }

    companion object {
        private const val MAX_VELOCITY = 1.52521
    }
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

fun Trajectory.Config.generate(points: Array<out Waypoint>): Trajectory =
        Pathfinder.generate(points, this)
