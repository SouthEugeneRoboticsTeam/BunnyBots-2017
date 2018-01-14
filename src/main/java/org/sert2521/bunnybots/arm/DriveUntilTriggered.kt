package org.sert2521.bunnybots.arm

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Preferences
import edu.wpi.first.wpilibj.command.Command
import org.sert2521.bunnybots.drivetrain.Drivetrain
import org.sert2521.bunnybots.util.frontSwitch

class DriveUntilTriggered(private val bucketTrigger: DigitalInput) : Command() {
    private val speed = Preferences.getInstance().getDouble("auto_drive_speed", -0.3)
    private var counter = 0
    private var counterWhenArmIsDown = 0
    private var hasArmHitBottom = false

    override fun initialize() {
//        leftDrive.tankDrive(speed * 2, speed * 2 + 0.025)
//        rightDrive.tankDrive(speed * 2, speed * 2 + 0.025)
    }

    override fun isFinished(): Boolean {
        return ((counter - counterWhenArmIsDown >= 50) and bucketTrigger.get() and frontSwitch.get().apply {
            if (!hasArmHitBottom) hasArmHitBottom = this
        })
    }

    override fun execute() {
        if (counter++ > 100) {
//            leftDrive.tankDrive(speed, speed + 0.05)
//            rightDrive.tankDrive(speed, speed + 0.05)
        }
        if (!hasArmHitBottom) counterWhenArmIsDown = counter
    }

    override fun end() {
        Drivetrain.stop()
    }
}
