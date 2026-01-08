package kpdev.rustlingleaves.model.move.drawing

import kpdev.rustlingleaves.model.skeleton.PlayerCard
import kpdev.rustlingleaves.model.config.Season
import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.Point

abstract class DrawCondition(
    val playerCard: PlayerCard
) {
    abstract fun isDrawingAreaAllowed(desiredArea: Area): Boolean
}

fun DEFAULT_DRAW_CONDITIONS(season: Season, playerCard: PlayerCard) = setOf(
    RiverDrawCondition(season, playerCard),
    AdjacentDrawCondition(playerCard),
    NoOverlapDrawCondition(playerCard)
)

class RiverDrawCondition(
    private val season: Season,
    playerCard: PlayerCard
) : DrawCondition(playerCard) {
    override fun isDrawingAreaAllowed(desiredArea: Area) : Boolean =
        season != Season.FALL ||
            ! playerCard.boardTemplate.river.overlapsWithArea(desiredArea)

}

class AdjacentDrawCondition(playerCard: PlayerCard) : DrawCondition(playerCard) {
    override fun isDrawingAreaAllowed(desiredArea: Area): Boolean =
        playerCard.areas.any { it.isAdjacentToArea(desiredArea) }
}

class NoOverlapDrawCondition(playerCard: PlayerCard) : DrawCondition(playerCard) {
    override fun isDrawingAreaAllowed(desiredArea: Area): Boolean =
        playerCard.areas.none { it.overlapsWithArea(desiredArea) }
}

class IncludeSinglePointDrawCondition(
    private val point: Point,
    playerCard: PlayerCard,
) : DrawCondition(playerCard) {
    override fun isDrawingAreaAllowed(desiredArea: Area) : Boolean =
        playerCard.areas.isNotEmpty() || desiredArea.includes(point)
}

class IncludeTypeDrawCondition(
    private val type: FieldType,
    playerCard: PlayerCard
) : DrawCondition(playerCard) {
    override fun isDrawingAreaAllowed(desiredArea: Area) : Boolean =
        desiredArea.fieldTypes.contains(type)
}