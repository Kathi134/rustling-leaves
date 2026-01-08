package kpdev.rustlingleaves.model.move.scoring

class AreaInclusionScoreStrategy(val countsIfIncluded: Boolean, val factor: Int) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int =
        factor * if(countsIfIncluded) getIncluded(context) else getNotIncluded(context)

    fun getIncluded(context: ScoringContext): Int =
        context.scoringTag.value

    fun getNotIncluded(context: ScoringContext): Int =
        context.scoringTag.max - context.scoringTag.value
}

class AllAreaInclusionScoreStrategy(val countsIfIncluded: Boolean, val bonus: Int)
: ConditionOrZeroScoreStrategy(FixedScoreStrategy(bonus)) {
    override fun checkCondition(context: ScoringContext): Boolean =
        if(countsIfIncluded)
            context.scoringTag.value == context.scoringTag.max
        else
            context.scoringTag.value == 0
}