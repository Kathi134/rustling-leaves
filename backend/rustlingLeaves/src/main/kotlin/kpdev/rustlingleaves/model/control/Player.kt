package kpdev.rustlingleaves.model.control

import jakarta.persistence.*
import kpdev.rustlingleaves.model.control.dice.DiceEvent
import kpdev.rustlingleaves.model.control.dice.PairedDiceResult
import kpdev.rustlingleaves.model.move.scoring.ScoringContext
import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.PlayerCard
import java.util.*

@Entity
class Player (
    @Id
    val id: UUID = UUID.randomUUID(),

    internal var hasStopped: Boolean = false,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "player_card_id")
    val playerCard: PlayerCard,

    @ElementCollection
    @CollectionTable(
        name = "player_dice_events",
        joinColumns = [JoinColumn(name = "player_id")]
    )
    val diceEventIdentifiers: MutableList<DiceEventIdentifier>,

    @OneToOne
    @JoinColumn(name = "lobby_member_id", nullable = false)
    private val lobbyMember: LobbyMember,

    @ManyToOne
    @JoinColumn(name = "game_engine_id")
    var gameEngine: GameEngine? = null
) {
    val numberOfRoundsPlayed : Int
        get() = playerCard.numberOfRoundsPlayed

    private val diceEvents : List<DiceEvent>
        get() = diceEventIdentifiers.map { it.toDiceEvent() }

    fun stop() {
        this.hasStopped = true
    }

    fun isStopped(): Boolean =
        hasStopped

    fun cleanUpDiceEvents() =
        diceEvents.forEach { c -> c.cleanUp(playerCard) }

    fun executeCallbackOnTrigger(res: PairedDiceResult, roundId: Int) =
        diceEvents
            .filter { it.triggerResult == res.white || it.triggerResult == res.green }
            .forEach { it.executeCallback(playerCard, roundId) }

    fun evalFinalScoring(contextSupplier: (FieldType) -> ScoringContext) =
        playerCard.evalFinalScoring(contextSupplier)
}
