package org.sert2521.bunnybots.arm

import edu.wpi.first.wpilibj.Preferences
import org.sert2521.bunnybots.drivetrain.drive
import org.sert2521.bunnybots.util.frontSwitch
import org.strongback.command.Command
import org.strongback.components.Switch

class DriveUntilTriggered(private val bucketTrigger: Switch) : Command(drive) {
    private val speed = Preferences.getInstance().getDouble("auto_drive_speed", -0.3)
    private var counter = 0
    private var counterWhenArmIsDown = 0
    private var hasArmHitBottom = false

    override fun initialize() {
        drive.tank(speed * 2, speed * 2 + 0.025)
    }

    override fun execute(): Boolean {
        if (counter++ > 100) drive.tank(speed, speed + 0.05)
        if (!hasArmHitBottom) counterWhenArmIsDown = counter
        return ((counter - counterWhenArmIsDown >= 50) and bucketTrigger.isTriggered and frontSwitch.isTriggered.apply {
            if (!hasArmHitBottom) hasArmHitBottom = this
        })
    }

    override fun end() {
        drive.tank(0.0, 0.0)
    }
}
