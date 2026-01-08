package kpdev.rustlingleaves.model.config

import kpdev.rustlingleaves.model.control.dice.DiceEvent
import kpdev.rustlingleaves.model.control.dice.FALL_DICE_EVENTS
import kpdev.rustlingleaves.model.control.dice.SPRING_DICE_EVENTS
import kpdev.rustlingleaves.model.move.drawing.Rectangle
import kpdev.rustlingleaves.model.skeleton.*

class BoardTemplateCollection {
    companion object {
        fun getDefaultRiver() =
            DEFAULT_RIVER

        private val DEFAULT_RIVER: River
            get() = River(setOf(
                Rectangle(Point(3,0), Point(4,4)),
                Rectangle(Point(4,4), Point(5,5)),
                Rectangle(Point(5,5), Point(6,9)),
            ))

        fun getDefaultFieldTypesForSeason(season: Season) : Matrix<Field> =
            BoardConfigParser.parseConfig(season).mapIndexedToType { idx, t -> Field(idx, t) }

//        this.mapIndexed { y, arr -> arr.mapIndexed { x, type -> Field(Point(x,y), type) }.toTypedArray() } .toTypedArray()

        fun getRandomFieldTypesForSeason(season: Season) : Matrix<Field> =
            throw NotImplementedError()

        fun getScoringTagsForSeason(season: Season): Set<ScoringTag> =
            when (season) {
                Season.WINTER -> WINTER_DEFAULT_SCORING_TAGS
                Season.SPRING -> SPRING_DEFAULT_SCORING_TAGS
                Season.SUMMER -> SUMMER_DEFAULT_SCORING_TAGS
                Season.FALL -> FALL_DEFAULT_SCORING_TAGS
            }

        fun getDefaultDiceEvents(season: Season): List<DiceEvent> =
            when(season) {
                Season.FALL -> FALL_DICE_EVENTS
                Season.SPRING -> SPRING_DICE_EVENTS
                else -> listOf()
            }
    }
}


