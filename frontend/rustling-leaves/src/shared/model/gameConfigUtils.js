import random from "../random"
import { SEASONS } from "./seasons"

export const START_POINTS = [
    {"x":1, "y":1},
    {"x":8, "y":8},
    {"x":1, "y":8},
    {"x":8, "y":1},
    {"x":2, "y":4},
    {"x":7, "y":5}
]


export function createGameConfigDto(memberId, season, startingPoint) {
    return {
        "memberId": memberId,
        "season": season ?? random(SEASONS),
        "startingPoint": random(START_POINTS)
    }
}