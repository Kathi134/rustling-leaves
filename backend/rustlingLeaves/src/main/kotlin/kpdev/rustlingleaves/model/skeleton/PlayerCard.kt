package kpdev.rustlingleaves.model.skeleton

import jakarta.persistence.*
import kpdev.rustlingleaves.model.config.BoardTemplateCollection
import kpdev.rustlingleaves.model.config.GameConfig
import kpdev.rustlingleaves.model.config.Season
import kpdev.rustlingleaves.model.control.DiceEventIdentifier
import kpdev.rustlingleaves.model.control.dice.AddIncludeTypeDrawConditionEvent
import kpdev.rustlingleaves.model.move.drawing.*
import kpdev.rustlingleaves.model.move.scoring.AreaInclusionScoreStrategy
import kpdev.rustlingleaves.model.move.scoring.ScoringContext
import java.util.*

@Entity
class PlayerCard(
    config : GameConfig,

    @Id
    val id : UUID = UUID.randomUUID(),

    @Enumerated(EnumType.ORDINAL)
    private val season: Season = config.season,

    @Embedded
    private val startingPoint: Point = config.startingPoint,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "board_template_id")
    val boardTemplate: BoardTemplate = BoardTemplate(
        season = config.season,
        fields =
            if(!config.random)
                BoardTemplateCollection.getDefaultFieldTypesForSeason(config.season)
            else
                BoardTemplateCollection.getRandomFieldTypesForSeason(config.season)
    ),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_card_id")
    val scoringTags: MutableSet<ScoringTag> = BoardTemplateCollection.getScoringTagsForSeason(config.season).toMutableSet(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name="player_card_id")
    val areas: MutableList<Area> = mutableListOf(),

    @ElementCollection
    @CollectionTable(
        name = "player_card_condition_triggers",
        joinColumns = [JoinColumn(name = "player_card_id")]
    )
    val drawConditionChangeTriggers : MutableSet<DiceEventIdentifier> = mutableSetOf()
) {
    val numberOfRoundsPlayed : Int
        get() = areas.size + scoringTags[FieldType.MISS].value

    fun drawConditions(roundId: Int) : MutableSet<DrawCondition> {
        val set = mutableSetOf<DrawCondition>(RiverDrawCondition(season, this))

        if(areas.size == 0) {
            set.add(IncludeSinglePointDrawCondition(startingPoint,this))
        } else {
            set.add(AdjacentDrawCondition(this))
            set.add(NoOverlapDrawCondition(this))
        }

        set.addAll(
            drawConditionChangeTriggers
                .map { it.toDiceEvent() as AddIncludeTypeDrawConditionEvent }
                .map { it.getCondition(this) }
        )

        return set
    }
    
    fun addDrawCondition(event: AddIncludeTypeDrawConditionEvent) {
        drawConditionChangeTriggers.add(DiceEventIdentifier.fromDiceEvent(event))
    }

    fun removeDrawCondition(event: AddIncludeTypeDrawConditionEvent) {
        drawConditionChangeTriggers.remove(DiceEventIdentifier.fromDiceEvent(event))
    }

    fun noMoveYetForRound(roundId: Int) =
        numberOfRoundsPlayed <= roundId

    fun storeMove(area: Area, tickedFieldType: FieldType, currentRoundId: Int) {
//        if(areas.isEmpty()) {
//            drawConditions.removeIf { it is IncludeSinglePointDrawCondition }
//            drawConditions.add(AdjacentDrawCondition(this))
//            drawConditions.add(NoOverlapDrawCondition(this))
//        }

        if(tickedFieldType != FieldType.MISS) {
            areas.add(area)
            val includedFieldsPerType = boardTemplate.drawArea(area, currentRoundId)
            includedFieldsPerType
                .filter { (t, _) -> t.scoringStrategy is AreaInclusionScoreStrategy }
                .forEach { (t, v) -> scoringTags[t].add(v, currentRoundId) }

            val tickedFieldsPerType = boardTemplate.tickFieldTypeInArea(area, tickedFieldType, currentRoundId)
            tickedFieldsPerType
                .forEach { (t, v) -> scoringTags[t].add(v, currentRoundId) }

            // evaluate river crossing
            if(area.crossesRiver(boardTemplate.river))
                scoringTags[FieldType.RIVER].inc(currentRoundId)
        } else {
            scoringTags[FieldType.MISS].inc(currentRoundId)
        }
    }

    fun evalFinalScoring(contextSupplier: (FieldType) -> ScoringContext) =
        scoringTags.associateWith { evalScoreForType(it.fieldType, contextSupplier(it.fieldType)) }

    fun evalScoreForType(type: FieldType, context: ScoringContext) : Int =
        type.scoringStrategy.getScore(context)

    fun isSaturated(): Boolean =
        scoringTags[FieldType.MISS].value == 6

}

