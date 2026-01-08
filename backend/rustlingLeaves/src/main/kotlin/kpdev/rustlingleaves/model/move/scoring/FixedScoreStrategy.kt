package kpdev.rustlingleaves.model.move.scoring

class FixedScoreStrategy(
    private val value: Int
) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int = value
}