package kpdev.rustlingleaves.model.move.scoring

import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.ScoringTag
import kpdev.rustlingleaves.model.skeleton.get


abstract class ConditionOrZeroScoreStrategy(
    val strategy: ScoreStrategy
) : ScoreStrategy {
    abstract fun checkCondition(context: ScoringContext): Boolean

    override fun getScore(context: ScoringContext): Int {
        if(checkCondition(context))
            return strategy.getScore(context)
        return 0
    }
}

class EvenFactoringScoreStrategy(
    factorValue: Int
) : ConditionOrZeroScoreStrategy(FactoringScoreStrategy(factorValue)) {
    override fun checkCondition(context: ScoringContext): Boolean =
        context.scoringTag.value % 2 == 0
}

class OddFactoringScoreStrategy(
    factorValue: Int
) : ConditionOrZeroScoreStrategy(FactoringScoreStrategy(factorValue)) {
    override fun checkCondition(context: ScoringContext): Boolean =
        context.scoringTag.value % 2 == 1
}

class ExactValueScoreStrategy(
    val targetValue: Int,
    bonusValue: Int,
) : ConditionOrZeroScoreStrategy(FixedScoreStrategy(bonusValue)) {
    override fun checkCondition(context: ScoringContext): Boolean =
        context.scoringTag.value == targetValue
}

class MoreThanOtherTypeScoreStrategy(
    val comparisonType: FieldType,
    factor: Int
) : ConditionOrZeroScoreStrategy(FactoringScoreStrategy(factor)) {
    override fun checkCondition(context: ScoringContext): Boolean =
        context.scoringTag.value >= context.allScoringTags[comparisonType].value
}