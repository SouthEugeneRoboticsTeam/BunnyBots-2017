package org.sert2521.bunnybots.arm

import com.ctre.MotorControl.CANTalon
import org.strongback.control.TalonController

class Toss(arm: TalonController, talon: CANTalon) : TalonResetCommandBase(talon, arm, 300) {
    private var counter = 0

    override fun initialize() {
        setArmSpeed(-.4)
    }

    override fun execute(): Boolean {
        setArmSpeed(-when (counter++) {
            0 -> .4
            in 1..10 -> .6
            else -> .8
        })
        return false
    }
}
