package org.sert2521.bunnybots.driving

import org.sert2521.bunnybots.util.LEFT_FRONT_MOTOR
import org.sert2521.bunnybots.util.RIGHT_FRONT_MOTOR
import org.sert2521.bunnybots.util.LEFT_REAR_MOTOR
import org.sert2521.bunnybots.util.RIGHT_REAR_MOTOR
import org.sert2521.bunnybots.util.leftJoystick
import org.sert2521.bunnybots.util.rightJoystick
import org.sert2521.bunnybots.util.scaledPitch
import org.sert2521.bunnybots.util.scaledRoll
import org.strongback.Strongback
import org.strongback.command.Command
import org.strongback.components.Motor
import org.strongback.hardware.Hardware
import java.util.function.Supplier
import org.strongback.drive.TankDrive as Drive

val drive = Drive(
        Motor.compose(
                Hardware.Motors.talonSRX(LEFT_FRONT_MOTOR),
                Hardware.Motors.talonSRX(RIGHT_FRONT_MOTOR)
        ),
        Motor.compose(
                Hardware.Motors.talonSRX(LEFT_REAR_MOTOR),
                Hardware.Motors.talonSRX(RIGHT_REAR_MOTOR)
        )
)
private val defaultDrive: Command
    get() = ArcadeDrive(drive, leftJoystick.scaledPitch, leftJoystick.scaledRoll)

fun initDrivetrain() {
    Strongback.submit(defaultDrive)
    Strongback.switchReactor().apply {
        onTriggeredSubmit(leftJoystick.thumb, Supplier {
            ArcadeDrive(drive, leftJoystick.scaledPitch, leftJoystick.scaledRoll)
        })
        onTriggeredSubmit(rightJoystick.thumb, Supplier {
            TankDrive(drive, leftJoystick.scaledPitch, rightJoystick.scaledPitch)
        })
    }
}
