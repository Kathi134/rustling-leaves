package kpdev.rustlingleaves.model.move.ticking

import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.move.drawing.Area
import kpdev.rustlingleaves.model.skeleton.FieldType

open class AreaContainsTickCondition(
    val type: FieldType,
    val lowerBoundIncluded: Int,
    val upperBoundExcluded: Int,
) : TickCondition {

    override fun checkTickable(drawnArea: Area?, diceResult: DiceResult?): Boolean {
        if(type == null || drawnArea == null)
            return false
        val num = drawnArea.numberFieldsOfType(type)
        return num in lowerBoundIncluded..<upperBoundExcluded
    }
}

class AreaContainsNoTickCondition(
    type: FieldType,
) : AreaContainsTickCondition(type, Int.MIN_VALUE, 0) {}

class AreaContainsAtLeastTickCondition(
    type: FieldType,
    atLeast: Int,
) : AreaContainsTickCondition(type, atLeast, Int.MAX_VALUE) {}
