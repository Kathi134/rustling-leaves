package kpdev.rustlingleaves.model.move.scoring

import kpdev.rustlingleaves.model.skeleton.ScoringTag
import kpdev.rustlingleaves.model.skeleton.get

open class CompareWithOpponentsScoreStrategy(private vararg val scores: Int) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int {
        val rank = getRankingDesc(context).indexOf(context.scoringTag)
            .coerceAtMost(context.allScoringTags.size) // +1 for self, -1 for index
        return scores.getOrElse(rank) { scores.last() }
    }

    open fun getRankingDesc(context: ScoringContext): List<ScoringTag> {
        return getAllResultsCombined(context)
            .sortedByDescending { it.value }
            .toList()
    }

    fun getAllResultsCombined(context: ScoringContext): MutableSet<ScoringTag> {
        val type = context.scoringTag.fieldType

        val results = context.otherPlayerCards
            ?.map { it.scoringTags[type] }?.toMutableSet()
        val myResult = context.scoringTag
        results?.add(myResult)

        return results ?: mutableSetOf()
    }
}

class AllOrNothingCompareWithOpponentsScoreStrategy(vararg scores: Int) : CompareWithOpponentsScoreStrategy(*scores) {
    override fun getRankingDesc(context: ScoringContext): List<ScoringTag> {
        val all = context.scoringTag.max

        return getAllResultsCombined(context)
            .filter { it.value == all }
            .sortedByDescending { it.lastTickedRoundIndex }
            .toList()
    }
}
