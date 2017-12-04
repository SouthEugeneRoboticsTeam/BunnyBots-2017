package org.sert2521.bunnybots.claw

import org.sert2521.bunnybots.util.SOLENOID_EXTEND
import org.sert2521.bunnybots.util.SOLENOID_RETRACT
import org.sert2521.bunnybots.util.leftJoystick
import org.sert2521.bunnybots.util.onTriggeredLifecycleSubmit
import org.strongback.Strongback
import org.strongback.components.Solenoid
import org.strongback.hardware.Hardware
import java.util.function.Supplier

private val claw: Solenoid = Hardware.Solenoids.doubleSolenoid(
        SOLENOID_EXTEND,
        SOLENOID_RETRACT,
        Solenoid.Direction.RETRACTING
)

fun initClaw() {
    claw.retract()
    Strongback.switchReactor().onTriggeredLifecycleSubmit(leftJoystick.trigger, Supplier {
        Grab(claw)
    })
}
