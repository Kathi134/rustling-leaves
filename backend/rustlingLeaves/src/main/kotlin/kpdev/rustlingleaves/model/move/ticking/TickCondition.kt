package kpdev.rustlingleaves.model.move.ticking

import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.move.drawing.Area

interface TickCondition {
    fun checkTickable(drawnArea: Area? = null, diceResult: DiceResult? = null) : Boolean
}

class TickableCondition : TickCondition {
    override fun checkTickable(drawnArea: Area?, diceResult: DiceResult?): Boolean = true
}

class NotTickableCondition : TickCondition {
    override fun checkTickable(drawnArea: Area?, diceResult: DiceResult?): Boolean = false
}
