package kpdev.rustlingleaves.model.control.dice

import jakarta.persistence.*
import java.util.*

@Entity
class Dice(
    @Id
    val id: UUID = UUID.randomUUID(),

    @ElementCollection
    @CollectionTable(
        name = "dice_roll_results_white",
        joinColumns = [JoinColumn(name = "dice_id")]
    )
    @MapKeyColumn(name = "round_id")
    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    val rollResultsWhiteDice : MutableMap<Int, DiceResult> = mutableMapOf(),

    @ElementCollection
    @CollectionTable(
        name = "dice_roll_results_green",
        joinColumns = [JoinColumn(name = "dice_id")]
    )
    @MapKeyColumn(name = "round_id")
    @Column(name = "result")
    @Enumerated(EnumType.STRING)
    val rollResultsGreenDice : MutableMap<Int, DiceResult> = mutableMapOf(),
) {

    fun roll(roundId : Int): PairedDiceResult =
        PairedDiceResult(
            white = rollResultsWhiteDice.computeIfAbsent(roundId) { DiceResult.entries.random() },
            green = rollResultsGreenDice.computeIfAbsent(roundId) { DiceResult.entries.random() }
        )

    fun isDiceForRoundNew(roundId : Int) : Boolean =
        rollResultsGreenDice.containsKey(roundId) && rollResultsWhiteDice.containsKey(roundId)
}