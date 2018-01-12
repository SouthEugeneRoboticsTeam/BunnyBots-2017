package org.sert2521.bunnybots.claw

import edu.wpi.first.wpilibj.DoubleSolenoid
import org.sert2521.bunnybots.util.SOLENOID_EXTEND
import org.sert2521.bunnybots.util.SOLENOID_RETRACT
import org.sert2521.bunnybots.util.bucketSwitch
import org.sert2521.bunnybots.util.rightJoystick

val claw: DoubleSolenoid = DoubleSolenoid(SOLENOID_EXTEND, SOLENOID_RETRACT)

fun initClaw() {
    if (rightJoystick.trigger || (rightJoystick.getRawButtonPressed(2) && bucketSwitch.get())) {
        Grab(claw)
    }
}
