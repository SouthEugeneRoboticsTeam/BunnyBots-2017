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
import org.sert2521.bunnybots.drivetrain.Drivetrain
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

@Suppress("MemberVisibilityCanPrivate", "HasPlatformType")
/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Robot : IterativeRobot() {
    override fun robotInit() {
        Drivetrain
    }

    val points = arrayOf(
            Waypoint(-3.0, 0.0, 0.0),
            Waypoint(-1.3, 1.8, 0.0),
            Waypoint(0.0, 0.0, Pathfinder.d2r(-90.0))
    )

    var config = TrajectoryConfig(1.25, 1.414, 60.0)
    var trajectory = config.generate(points)

    val modifier = TankModifier(trajectory).modify(0.86)

    val left = EncoderFollower(modifier.leftTrajectory)
    val right = EncoderFollower(modifier.rightTrajectory)

    val ahrs = AHRS(I2C.Port.kMXP)

    override fun autonomousInit() {
        Drivetrain.reset()
        left.reset()
        right.reset()

        ahrs.reset()

        left.configureEncoder(0, 8192, 0.15)
        left.configurePIDVA(0.8, 0.0, 0.0, 1.0 / 1.25, 0.0)

        right.configureEncoder(0, 8192, 0.15)
        right.configurePIDVA(0.8, 0.0, 0.0, 1.0 / 1.25, 0.0)
    }

    override fun autonomousPeriodic() {
        Scheduler.getInstance().run()

        val frontLeftPosition = Drivetrain.frontLeft.getSelectedSensorPosition(0) * -1
        val frontRightPosition = Drivetrain.frontRight.getSelectedSensorPosition(0) * -1

        val leftOut = left.calculate(frontLeftPosition)
        val rightOut = right.calculate(frontRightPosition)

        if (left.isFinished) {
            error("")
        } else {
            val angleDiff = Pathfinder.boundHalfDegrees(Pathfinder.r2d(left.heading) - ahrs.angle)
            val turn = 0.01 * angleDiff

            println("angle: $turn, left output: ${leftOut - turn}, right output: ${rightOut + turn}")
            Drivetrain.tank(leftOut - turn, rightOut + turn)
        }
    }

    override fun teleopInit() {
        Drivetrain.reset()
    }

    override fun teleopPeriodic() = Scheduler.getInstance().run()

    override fun disabledInit() {
        thread {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5))
            Drivetrain.setBreakMode(false)
        }
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

fun Trajectory.Config.generate(points: Array<out Waypoint>): Trajectory = Pathfinder.generate(points, this)
