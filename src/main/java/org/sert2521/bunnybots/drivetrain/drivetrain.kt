package org.sert2521.bunnybots.drivetrain

import org.sert2521.bunnybots.util.LEFT_FRONT_MOTOR
import org.sert2521.bunnybots.util.LEFT_REAR_MOTOR
import org.sert2521.bunnybots.util.RIGHT_FRONT_MOTOR
import org.sert2521.bunnybots.util.RIGHT_REAR_MOTOR
import org.sert2521.bunnybots.util.rightJoystick
import org.sert2521.bunnybots.util.scaledPitch
import org.sert2521.bunnybots.util.scaledRoll
import org.strongback.Strongback
import org.strongback.command.Command
import org.strongback.components.Motor
import org.strongback.hardware.Hardware
import java.util.function.Supplier
import org.strongback.drive.TankDrive as Drive

private val drive = Drive(
        Motor.compose(
                Hardware.Motors.talonSRX(LEFT_FRONT_MOTOR).enableBrakeMode(true),
                Hardware.Motors.talonSRX(LEFT_REAR_MOTOR).enableBrakeMode(true)
        ).invert(),
        Motor.compose(
                Hardware.Motors.talonSRX(RIGHT_FRONT_MOTOR).enableBrakeMode(true),
                Hardware.Motors.talonSRX(RIGHT_REAR_MOTOR).enableBrakeMode(true)
        )
)
private val defaultDrive: Command
    get() = ArcadeDrive(drive, rightJoystick.scaledPitch, rightJoystick.scaledRoll)

fun initDrivetrain() {
    Strongback.submit(defaultDrive)
    Strongback.switchReactor().apply {
        onTriggeredSubmit(rightJoystick.thumb, Supplier {
            ArcadeDrive(drive, rightJoystick.scaledPitch, rightJoystick.scaledRoll)
        })
        onTriggeredSubmit(rightJoystick.thumb, Supplier {
            TankDrive(drive, rightJoystick.scaledPitch, rightJoystick.scaledPitch)
        })
    }
}
