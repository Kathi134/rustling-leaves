package kpdev.rustlingleaves.model.move.scoring

import kpdev.rustlingleaves.model.skeleton.ScoringTag

class MultipleThresholdScoreStrategy(
    val thresholds : Array<Int>,
    val bonusValues : Array<Int>,
) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int {
//        thresholds
//            .filterIndexed { index, it -> scoringTag.value > it }
//            .mapIndexed { index, _ -> bonusValues.get(index) }
//            .sum()
        var sum = 0
        for(i in thresholds.indices)
        {
            if(context.scoringTag.value >= thresholds[i])
                sum += bonusValues[i]
        }
        return sum
    }
}