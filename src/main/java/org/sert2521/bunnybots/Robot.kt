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
        Slalom
    }

    val ahrs = AHRS(I2C.Port.kMXP)

    override fun autonomousInit() {
        Drivetrain.reset()
        breakModeUpdateTask?.cancel()

        Slalom.left.reset()
        Slalom.right.reset()

        ahrs.reset()
    }

    override fun autonomousPeriodic() {
        Scheduler.getInstance().run()

        val frontLeftPosition = Drivetrain.frontLeft.getSelectedSensorPosition(0) * -1
        val frontRightPosition = Drivetrain.frontRight.getSelectedSensorPosition(0) * -1

        val leftOut = Slalom.left.calculate(frontLeftPosition)
        val rightOut = Slalom.right.calculate(frontRightPosition)

        if (Slalom.isFinished) {
            Drivetrain.tank(0.0, 0.0)
        } else {
            val angleDiff =
                    Pathfinder.boundHalfDegrees(Pathfinder.r2d(Slalom.heading) - ahrs.angle)
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
}

@Suppress("FunctionName")
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

@Suppress("FunctionName")
fun TankModifier(source: Trajectory, wheelbaseWidth: Double): TankModifier =
        TankModifier(source).modify(wheelbaseWidth)

fun TankModifier.split(): Pair<EncoderFollower, EncoderFollower> =
        EncoderFollower(leftTrajectory) to EncoderFollower(rightTrajectory)

fun Array<Trajectory.Segment>.reduce(n: Int): List<Trajectory.Segment> {
    var result = toList()
    while (result.size > n) result = result.filterIndexed { i, _ -> i % 2 == 0 }
    return result
}

abstract class PathInitializer {
    protected abstract val trajectory: Trajectory
    protected abstract val followers: Pair<EncoderFollower, EncoderFollower>

    val left get() = followers.first
    val right get() = followers.second
    val isFinished get() = left.isFinished
    val heading get() = left.heading

    protected fun logGeneratedPoints() {
        println("""
                |Generated ${trajectory.segments.size} points:
                ${trajectory.segments.reduce(50).joinToString("\n") { "${it.x}, ${it.y}" }}
                |""".trimMargin())
    }
}

object Slalom : PathInitializer() {
    private const val MAX_VELOCITY = 1.4

    override val trajectory = TrajectoryConfig(MAX_VELOCITY, 0.1, 60.0).generate(arrayOf(
            Waypoint(0.0, 0.0, 0.0),
            Waypoint(1.5, 0.0, 0.0),
            Waypoint(3.0, -2.17, 0.0),
            Waypoint(4.5, -0.55, 0.0),
            Waypoint(7.17, -2.17, 0.0)
    ))
    override val followers = TankModifier(trajectory, 0.86).split()

    init {
        logGeneratedPoints()

        left.configureEncoder(0, 8192, 0.15)
        left.configurePIDVA(2.75, 0.0, 0.125, 1 / MAX_VELOCITY, 1.0)

        right.configureEncoder(0, 8192, 0.15)
        right.configurePIDVA(2.75, 0.0, 0.125, 1 / MAX_VELOCITY, 1.0)
    }
}
