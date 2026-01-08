package kpdev.rustlingleaves.model.move.scoring

import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.get
import kotlin.math.max

class MultiplyWithTypesScoreStrategy(val types: Set<FieldType>) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int {
        val set =
            if(!types.contains(context.scoringTag.fieldType))
                types.plus(context.scoringTag.fieldType)
            else
                types
        return set.map { context.allScoringTags[it].value }.reduce(Int::times)
    }
}

abstract class PairWithTypeScoreStrategy(val pairType: FieldType) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int {
        val ownVal = context.scoringTag.value
        val pairVal = context.allScoringTags[pairType].value
        return computeScoreFromBothPairValues(ownVal, pairVal)
    }

    abstract fun computeScoreFromBothPairValues(partnerA : Int, partnerB : Int): Int
}

open class FactoringPairWithTypeStrategy(
    private val factor: Int,
    pairType: FieldType
) : PairWithTypeScoreStrategy(pairType) {

    override fun computeScoreFromBothPairValues(partnerA: Int, partnerB: Int): Int =
        max(partnerA, partnerB) * factor
}

class ExactAmountFactoringPairWithTypeStrategy(
    private val factor: Int,
    pairType: FieldType
) : FactoringPairWithTypeStrategy(factor, pairType) {
    override fun computeScoreFromBothPairValues(partnerA : Int, partnerB : Int): Int =
        if(partnerA == partnerB) partnerA * factor else 0
}