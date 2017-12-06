package org.sert2521.bunnybots.claw

import org.sert2521.bunnybots.util.SOLENOID_EXTEND
import org.sert2521.bunnybots.util.SOLENOID_RETRACT
import org.sert2521.bunnybots.util.bucketSwitch
import org.sert2521.bunnybots.util.leftJoystick
import org.sert2521.bunnybots.util.onTriggeredLifecycleSubmit
import org.sert2521.bunnybots.util.rightJoystick
import org.strongback.Strongback
import org.strongback.components.Solenoid
import org.strongback.components.Switch
import org.strongback.hardware.Hardware
import java.util.function.Supplier

val claw: Solenoid = Hardware.Solenoids.doubleSolenoid(
        SOLENOID_EXTEND,
        SOLENOID_RETRACT,
        Solenoid.Direction.RETRACTING
)

fun initClaw() {
    Strongback.switchReactor().onTriggeredLifecycleSubmit(
            Switch.or(
                    Switch.or(leftJoystick.trigger, rightJoystick.trigger),
                    Switch.and(Switch.or(leftJoystick.thumb, rightJoystick.thumb), bucketSwitch)
            ),
            Supplier { Grab(claw) }
    )
}
