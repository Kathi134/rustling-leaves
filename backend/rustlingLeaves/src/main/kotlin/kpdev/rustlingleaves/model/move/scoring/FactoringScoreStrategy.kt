package kpdev.rustlingleaves.model.move.scoring

import kpdev.rustlingleaves.model.skeleton.ScoringTag

open class FactoringScoreStrategy(
    private val factorValue: Int
) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int {
        return factorValue * context.scoringTag.value
    }
}

class FactoringAndBonusScoreStrategy(
    factorValue: Int,
    private val bonusThreshold: Int,
    private val bonusValue: Int,
) : FactoringScoreStrategy(factorValue) {
    override fun getScore(context: ScoringContext): Int {
        var score = super.getScore(context)
        if(context.scoringTag.value >= bonusThreshold)
            score += bonusValue
        return score
    }
}
