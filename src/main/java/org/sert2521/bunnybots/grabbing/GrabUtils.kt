package org.sert2521.bunnybots.driving

import org.sert2521.bunnybots.util.leftJoystick
import org.strongback.command.Command
import org.strongback.components.Solenoid
import org.strongback.hardware.Hardware


private val grab = Hardware.Solenoids.doubleSolenoid(0, 0, Solenoid.Direction.RETRACTING)

private val defaultGrab: Command
    get() = Grab(grab, leftJoystick.trigger)



// onTriggeredLifeCycle