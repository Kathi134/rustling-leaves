package kpdev.rustlingleaves.model.config

import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.ScoringTag

// TODO : inclusion fields max must be all crossable fields of this type

val WINTER_DEFAULT_SCORING_TAGS =
    setOf(
        ScoringTag(FieldType.TREE_WINTER, 18),
        ScoringTag(FieldType.SNOWFOX, 10),
        ScoringTag(FieldType.BIRD_WINTER, 6),
        ScoringTag(FieldType.BERRY, 6),
        ScoringTag(FieldType.ICICLE, 6),
        ScoringTag(FieldType.HOLLY, 8),
        ScoringTag(FieldType.MOOSE, 10),
        ScoringTag(FieldType.SNOWDROP, 9),
        ScoringTag(FieldType.CLOUD, 9),
        ScoringTag(FieldType.RIVER, 12),
        ScoringTag(FieldType.MISS, 6)
    )

val SPRING_DEFAULT_SCORING_TAGS =
    setOf(
        ScoringTag(FieldType.TREE_SPRING, 15),
        ScoringTag(FieldType.BEE, 6),
        ScoringTag(FieldType.FLOWER, 9),
        ScoringTag(FieldType.BUTTERFLY, 10),
        ScoringTag(FieldType.BIRD_LEFT, 6), // TODO
        ScoringTag(FieldType.RAINBOW, 7),
        ScoringTag(FieldType.CLOUD, 6),
        ScoringTag(FieldType.SEED, 7),
        ScoringTag(FieldType.BEAR, 6),
        ScoringTag(FieldType.RIVER, 9),
        ScoringTag(FieldType.MISS, 6)
    )

val SUMMER_DEFAULT_SCORING_TAGS =
    setOf(
        ScoringTag(FieldType.TREE_SUMMER, 15),
        ScoringTag(FieldType.DAISY, 15),
        ScoringTag(FieldType.CLOVER_LEAF, 4),
        ScoringTag(FieldType.FRUIT_APPLE, 5),
        ScoringTag(FieldType.FRUIT_PEACH, 5),
        ScoringTag(FieldType.FRUIT_CHERRY, 5),
        ScoringTag(FieldType.FIREFLY, 5),
        ScoringTag(FieldType.MOSQUITO, 8),
        ScoringTag(FieldType.SNAKE, 5),
        ScoringTag(FieldType.MISS, 6)
    )

val FALL_DEFAULT_SCORING_TAGS =
    setOf(
        ScoringTag(FieldType.MUSHROOM_PORCINI, 14),
        ScoringTag(FieldType.MUSHROOM_TOADSTOOL,11),
        ScoringTag(FieldType.CONE, 8),
        ScoringTag(FieldType.TREE_CONIFER, 18), // TODO
        ScoringTag(FieldType.ACORN, 8),
        ScoringTag(FieldType.SQUIRREL, 5),
        ScoringTag(FieldType.TREE_LEAFED, 11),
        ScoringTag(FieldType.LEAF, 11),
        ScoringTag(FieldType.MISS, 6)
    )