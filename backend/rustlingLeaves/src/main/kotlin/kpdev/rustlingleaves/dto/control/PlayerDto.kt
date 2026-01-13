package kpdev.rustlingleaves.dto.control

import kpdev.rustlingleaves.dto.skeleton.BoardTemplateDto
import kpdev.rustlingleaves.dto.skeleton.PlayerCardDtoResponse
import kpdev.rustlingleaves.dto.skeleton.ScoringTagDto
import kpdev.rustlingleaves.dto.skeleton.toDto
import kpdev.rustlingleaves.model.control.Player
import kpdev.rustlingleaves.model.move.drawing.Rectangle
import java.util.*

data class PlayerDto (
    val id: UUID,
    val stopped: Boolean,
    val playerCard: PlayerCardDtoResponse,
)

fun Player.toDto(): PlayerDto =
    PlayerDto(id, hasStopped, playerCard.toDto())

data class PlayerDetailedDtoResponse(
    val boardTemplate: BoardTemplateDto,
    val scoringTags: List<ScoringTagDto>,
    val areas: List<Rectangle>,
    val numberOfRoundsPlayed: Int,
    val hasStopped: Boolean
)

fun Player.toDetailedDto(): PlayerDetailedDtoResponse =
    PlayerDetailedDtoResponse(
        this.playerCard.boardTemplate.toDto(),
        this.playerCard.scoringTags.map { it.toDto() },
        this.playerCard.areas.map { it.rectangle },
        this.numberOfRoundsPlayed,
        this.hasStopped
    )