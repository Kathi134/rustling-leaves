package kpdev.rustlingleaves.model.config

import kpdev.rustlingleaves.model.control.LobbyMember
import kpdev.rustlingleaves.model.skeleton.Point

data class GameConfig(
    val member: LobbyMember,
    val season: Season,
    val random: Boolean,
    val startingPoint: Point
)
