package org.sert2521.bunnybots.util

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.buttons.JoystickButton

val leftJoystick: Joystick = Joystick(LEFT_STICK_PORT)
val rightJoystick: Joystick = Joystick(RIGHT_STICK_PORT)
val frontSwitch: DigitalInput = DigitalInput(0)
//val rearSwitch: JoystickButton = Hardware.Switches.normallyClosed(1)
val bucketSwitch: DigitalInput = DigitalInput(2)
val tossSwitch: JoystickButton = JoystickButton(leftJoystick, 3)
