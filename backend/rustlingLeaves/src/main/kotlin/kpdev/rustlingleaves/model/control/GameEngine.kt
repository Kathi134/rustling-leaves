package kpdev.rustlingleaves.model.control

import jakarta.persistence.*
import kpdev.rustlingleaves.model.config.GameConfig
import kpdev.rustlingleaves.model.control.dice.Dice
import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.control.dice.PairedDiceResult
import kpdev.rustlingleaves.model.move.drawing.Area
import kpdev.rustlingleaves.model.move.drawing.Rectangle
import kpdev.rustlingleaves.model.move.scoring.ScoringContext
import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.PlayerCard
import java.util.*

@Entity
class GameEngine(
    config: List<GameConfig>,

    @Id
    val id: UUID = UUID.randomUUID(),

    @OneToMany(
        mappedBy = "gameEngine",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    private val players: Set<Player> =
        config.map { it.member.playerObject!! }.toSet(),

    @OneToOne(cascade = [CascadeType.ALL], optional = false, orphanRemoval = true)
    @JoinColumn(name = "dice_id")
    private val dice: Dice = Dice(),

    private var currentRoundId: Int = 0,
) {
    val isFinished : Boolean
        get() = players.all { it.hasStopped }

//    fun isNewRollForRoundAvailable() : Boolean =
//        dice.isDiceForRoundNew(currentRoundId)

    fun rollDice() : PairedDiceResult {
        players.forEach { it.cleanUpDiceEvents() }
        val res = dice.roll(currentRoundId)
        players.forEach { it.executeCallbackOnTrigger(res, currentRoundId) }
        return res
    }

    fun isDrawingAreaAllowed(player: Player, desiredArea: Area) : Boolean =
        player.playerCard
            .drawConditions(currentRoundId)
            .all { cond -> cond.isDrawingAreaAllowed(desiredArea) }

    fun getReasonForDrawingAreaUnallowed(player: Player, desiredArea: Area) =
        player.playerCard.drawConditions(currentRoundId)
            .filter { !it.isDrawingAreaAllowed(desiredArea) }
            .map { it::class.simpleName }

    fun getTickableFieldTypesInArea(area: Area) : Set<FieldType> =
        area.fieldTypes.filter { it.tickCondition.checkTickable(area) }.toSet()

    fun getPointsOfFieldTypeInRectangle(player: Player, type: FieldType, rect: Rectangle) =
        player.playerCard.boardTemplate.getPointsOfFieldTypeInRectangle(rect, type)

    fun storeMove(player: Player, area: Area, tickedFieldType: FieldType, roundId: Int) : PlayerCard {
        val card = player.playerCard

        if(roundId != currentRoundId)
            return card
        if(player.isStopped())
            return card
        if(!card.noMoveYetForRound(roundId))
            return card
        if(!isDrawingAreaAllowed(player, area) || !getTickableFieldTypesInArea(area).contains(tickedFieldType))
            return card

        if(tickedFieldType == FieldType.MISS && player.playerCard.isSaturated())
            player.stop()

        card.storeMove(area, tickedFieldType, currentRoundId)
        if(players.all { it.numberOfRoundsPlayed == currentRoundId })
            currentRoundId++
        return card
    }

    fun stopGameForPlayer(player: Player, roundId: Int) : PlayerCard {
        if(roundId == currentRoundId)
            player.stop()
        return player.playerCard
    }

    private fun getForeignPlayerCards(playerCard: PlayerCard) =
        players.map { it.playerCard }.filterNot { it == playerCard }.toSet()

    fun evalScoreForType(player: Player, type: FieldType) : Int {
        val playerCard = player.playerCard
        val context = ScoringContext(type, playerCard, getForeignPlayerCards(playerCard))
        return playerCard.evalScoreForType(type, context)
    }

    val finalScoring : Map<Player, Map<FieldType, Int>>
        get() = players
            .associateWith { p ->
                p.evalFinalScoring { t ->
                    ScoringContext(t, p.playerCard, getForeignPlayerCards(p.playerCard))
                }.mapKeys { it.key.fieldType }
            }
}

