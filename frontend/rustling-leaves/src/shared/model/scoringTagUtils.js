const SPRING = [
    "TREE_SPRING",
    "BEE",
    "FLOWER",
    "BUTTERFLY",
    "BIRD_LEFT",
    "BIRD_RIGHT",
    "RAINBOW",
    "CLOUD",
    "SEED",
    "BEAR",
    "RIVER",
    "MISS"
]

export const sortSpringTags = (a,b) => SPRING.indexOf(a.fieldTypeDto.enumName) - SPRING.indexOf(b.fieldTypeDto.enumName)