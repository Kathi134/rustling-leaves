package kpdev.rustlingleaves.model.skeleton

import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.move.scoring.*
import kpdev.rustlingleaves.model.move.ticking.*


// TODO:
// maybe seperate tickFieldType from ScoreFieldType (birdLeft, birdRight)
// in eval scoreChange, any ScoreFieldType that includes my tickFieldType
enum class FieldType(
    val scoringStrategy: ScoreStrategy,
    val tickGroup: TickGroup,
    private val tickConditionFactory : (FieldType) -> TickCondition
) {
    START_LEAF(tickCondition = NotTickableCondition()),
    MISS(tickCondition = NotTickableCondition()),
    CLOUD(tickCondition = NotTickableCondition()),
    RIVER(tickCondition = NotTickableCondition()),

    TREE_SPRING(FactoringAndBonusScoreStrategy(1,10,5)),
    FLOWER,
    BEE(BeeAndFlowerScoreStrategy()),
    BUTTERFLY(EvenFactoringScoreStrategy(2)),
    BIRD_LEFT, // FactoringPairWithTypeStrategy(5, BIRD_RIGHT)
    BIRD_RIGHT(FactoringPairWithTypeStrategy(5, BIRD_LEFT), tickGroup = TickGroup(BIRD_LEFT)),
    RAINBOW(AllTickedScoreStrategy(30)),
    SEED(SeedAndCloudScoreStrategy()),
    BEAR(AreaInclusionScoreStrategy(false, 3), NotTickableCondition()),

    TREE_SUMMER(FactoringScoreStrategy(2), DiceValueCondition(DiceResult.CLOUD_VALUES)),
    DAISY(OddFactoringScoreStrategy(2)),
    CLOVER_LEAF(AllOrNothingCompareWithOpponentsScoreStrategy(20, 10)),
    FRUIT_APPLE, // MultiplyWithTypesScoreStrategy(setOf(FRUIT_APPLE, FRUIT_CHERRY, FRUIT_PEACH))
    FRUIT_CHERRY, // MultiplyWithTypesScoreStrategy(setOf(FRUIT_APPLE, FRUIT_CHERRY, FRUIT_PEACH))
    FRUIT_PEACH(MultiplyWithTypesScoreStrategy(setOf(FRUIT_APPLE, FRUIT_CHERRY, FRUIT_PEACH))),
    FIREFLY(FactoringScoreStrategy(3), DiceValueCondition(DiceResult.NONE_CLOUD_VALUES)),
    MOSQUITO(AllAreaInclusionScoreStrategy(false, 15), NotTickableCondition()),
    SNAKE(AreaInclusionScoreStrategy(false, 3), NotTickableCondition()),

    MUSHROOM_TOADSTOOL(FactoringScoreStrategy(-2), NotTickableCondition()),
    MUSHROOM_PORCINI(FactoringScoreStrategy(3), tickGroup = TickGroup(MUSHROOM_TOADSTOOL)),
    CONE(MultipleThresholdScoreStrategy(arrayOf(4,8), arrayOf(12, 12))),
    TREE_CONIFER, // TODO
    SQUIRREL, // MultiplyWithTypesScoreStrategy(setOf(ACORN, SQUIRREL))
    ACORN(MultiplyWithTypesScoreStrategy(setOf(ACORN, SQUIRREL))),
    TREE_LEAFED, // FactoringPairWithTypeStrategy(5, LEAF)
    LEAF(FactoringPairWithTypeStrategy(5, TREE_LEAFED)),
    STORM(tickCondition = NotTickableCondition()),

    SNOWFLAKE(tickCondition = NotTickableCondition()),
    TREE_WINTER(FactoringScoreStrategy(1), AreaContainsAtLeastTickCondition(SNOWFLAKE, 1)),
    SNOWFOX(scoringStrategy = FactoringScoreStrategy(3), tickGroup = TickGroup(), tickConditionFactory = { AreaContainsTickCondition(it, 1, 1)}),
    BIRD_WINTER, // ExactAmountFactoringPairWithTypeStrategy(5, BERRY)
    BERRY(ExactAmountFactoringPairWithTypeStrategy(5, BIRD_WINTER)),
    MOOSE(AllAreaInclusionScoreStrategy(true, 12), NotTickableCondition()),
    ICICLE(ExactValueScoreStrategy(5, 15)),
    HOLLY, // TODO
    SNOWDROP(MoreThanOtherTypeScoreStrategy(CLOUD, 3));

    constructor(
        scoringStrategy: ScoreStrategy = FixedScoreStrategy(0),
        tickCondition: TickCondition = TickableCondition(),
        tickGroup: TickGroup = TickGroup(),
    ) : this (scoringStrategy, tickGroup, { tickCondition })

    val tickCondition: TickCondition by lazy {
        tickConditionFactory(this)
    }
}