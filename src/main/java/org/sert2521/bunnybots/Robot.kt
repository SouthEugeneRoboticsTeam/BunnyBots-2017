package org.sert2521.bunnybots

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.wpilibj.I2C
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Waypoint
import org.sert2521.bunnybots.drivetrain.Drivetrain
import org.sertain.Robot
import org.sertain.hardware.encoderPosition
import org.sertain.util.PathInitializer
import org.sertain.util.TankModifier
import org.sertain.util.TrajectoryConfig
import org.sertain.util.generate
import org.sertain.util.split

/**
 * This is the main robot class which calls various methods depending on the current game stage.
 */
class Alluminati : Robot() {
    private val ahrs = AHRS(I2C.Port.kMXP)

    override fun onCreate() {
        Drivetrain
        Slalom
    }

    override fun onStart() {
        Slalom.left.reset()
        Slalom.right.reset()

        ahrs.reset()
    }

    override fun executeAuto() {
        val frontLeftPosition = Drivetrain.frontLeft.encoderPosition * -1
        val frontRightPosition = Drivetrain.frontRight.encoderPosition * -1

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
}

object Slalom : PathInitializer() {
    private const val MAX_VELOCITY = 1.4

    override val trajectory = TrajectoryConfig(MAX_VELOCITY, 0.1, 60.0).generate(arrayOf(
            Waypoint(0.0, 0.0, 0.0),
            Waypoint(3.5, 0.0, Pathfinder.d2r(-45.0)),
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
