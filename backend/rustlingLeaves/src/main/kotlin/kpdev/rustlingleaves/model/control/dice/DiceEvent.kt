package kpdev.rustlingleaves.model.control.dice

import kpdev.rustlingleaves.model.move.drawing.IncludeTypeDrawCondition
import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.PlayerCard
import kpdev.rustlingleaves.model.skeleton.get

abstract class DiceEvent(
    val triggerResult: DiceResult
) {
    abstract fun executeCallback(card: PlayerCard, roundId: Int) : Any
    open fun cleanUp(card: PlayerCard) {}
}

// Spring: Cloud -> Tick all Clouds
class AutoTickFieldEvent(triggerResult: DiceResult) : DiceEvent(triggerResult) {
    override fun executeCallback(card: PlayerCard, roundId: Int) =
        card.scoringTags[FieldType.CLOUD].inc(roundId)
}

val SPRING_DICE_EVENTS = DiceResult.CLOUD_VALUES.map { AutoTickFieldEvent(it) }.toList()


// Fall: Cloud -> Add IncludeTypeDrawCondition(STORM, 1)
class AddIncludeTypeDrawConditionEvent(
    val type: FieldType,
    triggerResult: DiceResult
) : DiceEvent(triggerResult) {
    private val conditions : MutableMap<PlayerCard, IncludeTypeDrawCondition> = mutableMapOf()

    fun getCondition(card: PlayerCard) =
        conditions.computeIfAbsent(card) { IncludeTypeDrawCondition(type, it) }

    override fun executeCallback(card: PlayerCard, roundId: Int) =
        card.addDrawCondition(this)

    override fun cleanUp(card: PlayerCard) {
        card.removeDrawCondition(this)
    }
}

val FALL_DICE_EVENTS = DiceResult.CLOUD_VALUES.map { AddIncludeTypeDrawConditionEvent(FieldType.STORM, it) }.toList()
