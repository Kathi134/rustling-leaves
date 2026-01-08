package kpdev.rustlingleaves.model.move.ticking

import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.move.drawing.Area

class DiceValueCondition(
    val targetValues: Set<DiceResult>
) : TickCondition {
    override fun checkTickable(drawnArea: Area?, diceResult: DiceResult?): Boolean =
        targetValues.any { it == diceResult }
}