package kpdev.rustlingleaves.model.move.scoring

class AllTickedScoreStrategy(
    val bonusValue: Int,
) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int =
        if (context.scoringTag.value == context.scoringTag.max)
            bonusValue
        else
            0
}