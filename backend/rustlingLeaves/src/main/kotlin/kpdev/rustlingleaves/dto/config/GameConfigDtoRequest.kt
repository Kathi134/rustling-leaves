package kpdev.rustlingleaves.dto.config

import kpdev.rustlingleaves.model.config.GameConfig
import kpdev.rustlingleaves.model.config.Season
import kpdev.rustlingleaves.model.skeleton.Point
import kpdev.rustlingleaves.service.LobbyService
import java.util.UUID

data class GameConfigDtoRequest (
    val memberId: UUID,
    val season: Season,
    val random: Boolean,
    val startingPoint: Point
)

fun GameConfigDtoRequest.asEntity(memberService: LobbyService): GameConfig =
    GameConfig(
        member = memberService.getMember(memberId),
        season = season,
        random = random,
        startingPoint = startingPoint
    )
