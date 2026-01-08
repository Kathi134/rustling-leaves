package kpdev.rustlingleaves.model.control

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import kpdev.rustlingleaves.model.control.dice.AddIncludeTypeDrawConditionEvent
import kpdev.rustlingleaves.model.control.dice.DiceEvent
import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.skeleton.FieldType

@Embeddable
class DiceEventIdentifier(
    val className: String = "",

    @Enumerated(EnumType.STRING)
    val triggerResult: DiceResult = DiceResult.OPTION_1,

    @Enumerated(EnumType.STRING)
    val type: FieldType? = null
) {
    companion object {
        fun fromDiceEvent(event: DiceEvent) : DiceEventIdentifier =
            DiceEventIdentifier(
                className = event.javaClass.name,
                triggerResult = event.triggerResult,
                type =
                    if(event is AddIncludeTypeDrawConditionEvent)
                        event.type
                    else
                        null
            )
    }

    fun toDiceEvent() : DiceEvent {
        val constructor = Class.forName(className).declaredConstructors[0]
        val inst =
            if(this.type != null)
                constructor.newInstance(this.type, triggerResult)
            else
                constructor.newInstance(triggerResult)
        return inst as DiceEvent
    }
}

