package kpdev.rustlingleaves.model.move.scoring

interface ScoreStrategy {
    fun  getScore(context: ScoringContext) : Int
}



