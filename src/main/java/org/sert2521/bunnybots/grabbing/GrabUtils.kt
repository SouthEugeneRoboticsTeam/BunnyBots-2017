package org.sert2521.bunnybots.driving

import org.strongback.components.Solenoid
import org.strongback.hardware.Hardware


private val grab = Hardware.Solenoids.doubleSolenoid(0, 0, Solenoid.Direction.RETRACTING)
