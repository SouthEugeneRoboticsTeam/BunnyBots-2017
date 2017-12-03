package org.sert2521.bunnybots.util

import edu.wpi.first.wpilibj.Preferences
import org.strongback.components.ui.ContinuousRange
import org.strongback.components.ui.FlightStick
import org.strongback.hardware.Hardware

val leftJoystick: FlightStick = Hardware.HumanInterfaceDevices.logitechAttack3D(LEFT_STICK_PORT)
val rightJoystick: FlightStick = Hardware.HumanInterfaceDevices.logitechAttack3D(RIGHT_STICK_PORT)
val frontSwitch = Hardware.Switches.normallyClosed(0)
val rearSwitch = Hardware.Switches.normallyClosed(1)

val FlightStick.scaledThrottle: ContinuousRange
    get() = throttle.invert().map { (it + 1) * .5 }
val FlightStick.scaledPitch: ContinuousRange
    get() = pitch.scale(Preferences.getInstance().getDouble("speed_scalar", 1.0))
val FlightStick.scaledRoll: ContinuousRange
    get() = roll.scale(Preferences.getInstance().getDouble("speed_scalar", 1.0))
