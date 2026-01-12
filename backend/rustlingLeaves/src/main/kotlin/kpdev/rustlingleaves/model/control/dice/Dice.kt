package kpdev.rustlingleaves.model.control.dice

import jakarta.persistence.*
import java.util.*

@Entity
class Dice(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ElementCollection
    @CollectionTable(
        name = "dice_roll_results",
        joinColumns = [JoinColumn(name = "dice_id")]
    )
    @MapKeyColumn(name = "round_id")
    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    val rollResults : MutableMap<Int, DiceResult> = mutableMapOf(),
) {

    @Synchronized
    fun roll(roundId: Int): DiceResult =
        rollResults.getOrPut(roundId) { DiceResult.entries.random() }

    fun isDiceForRoundNew(roundId : Int) : Boolean =
        rollResults.containsKey(roundId)
}

