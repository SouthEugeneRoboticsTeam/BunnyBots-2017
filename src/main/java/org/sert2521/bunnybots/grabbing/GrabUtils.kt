package org.sert2521.bunnybots.driving

import org.sert2521.bunnybots.util.leftJoystick
import org.sert2521.bunnybots.util.onTriggeredLifecycleSubmit
import org.strongback.Strongback
import org.strongback.command.Command
import org.strongback.components.Solenoid
import org.strongback.hardware.Hardware
import java.util.function.Supplier


private val grab = Hardware.Solenoids.doubleSolenoid(0, 0, Solenoid.Direction.RETRACTING)

fun initGrab() {
    Strongback.switchReactor().apply {
        onTriggeredLifecycleSubmit(leftJoystick.trigger, Supplier {
            Grab(grab, leftJoystick.trigger)
        })
    }
}

// onTriggeredLifeCycle