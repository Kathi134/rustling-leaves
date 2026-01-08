package kpdev.rustlingleaves.model.move.scoring

import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.get

abstract class ConditionedByOtherTypeScoreStrategy(private val otherType: FieldType) : ScoreStrategy {
    override fun getScore(context: ScoringContext): Int {
        val fieldsOfBaseType = context.scoringTag.value
        val fieldsOfOtherType = context.allScoringTags[otherType].value
        return evaluateCondition(fieldsOfBaseType, fieldsOfOtherType)
    }

    abstract fun evaluateCondition(fieldsOfBaseType: Int, fieldsOfOtherType: Int): Int
}


class BeeAndFlowerScoreStrategy : ConditionedByOtherTypeScoreStrategy(FieldType.FLOWER) {
    override fun evaluateCondition(fieldsOfBaseType: Int, fieldsOfOtherType: Int): Int =
        fieldsOfBaseType *
            if(fieldsOfOtherType <= 2)
                0
            else if (fieldsOfOtherType <= 5)
                3
            else if(fieldsOfOtherType <= 8)
                4
            else if(fieldsOfOtherType == 9)
                5
            else
                throw IllegalStateException("Illegal amount of flowers: $fieldsOfOtherType")
}


class SeedAndCloudScoreStrategy : ConditionedByOtherTypeScoreStrategy(FieldType.CLOUD) {
    override fun evaluateCondition(fieldsOfBaseType: Int, fieldsOfOtherType: Int): Int =
        fieldsOfBaseType *
            if(fieldsOfOtherType <= 2)
                0
            else if (fieldsOfOtherType <= 5)
                2
            else
                4
}

