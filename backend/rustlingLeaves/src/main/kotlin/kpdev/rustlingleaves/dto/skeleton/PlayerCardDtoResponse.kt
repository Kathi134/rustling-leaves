package kpdev.rustlingleaves.dto.skeleton

import kpdev.rustlingleaves.model.move.drawing.Rectangle
import kpdev.rustlingleaves.model.skeleton.PlayerCard

data class PlayerCardDtoResponse (
    val scoringTags: List<ScoringTagDto>,
    val areas: List<Rectangle>,
)

fun PlayerCard.toDto(): PlayerCardDtoResponse =
    PlayerCardDtoResponse(
        this.scoringTags.map { it.toDto() },
        this.areas.map { it.rectangle }
    )

data class PlayerCardDetailedDtoResponse(
    val boardTemplate: BoardTemplateDto,
    val scoringTags: List<ScoringTagDto>,
    val areas: List<Rectangle>,
    val numberOfRoundsPlayed: Int,
)

fun PlayerCard.toDetailedDto(): PlayerCardDetailedDtoResponse =
    PlayerCardDetailedDtoResponse(
        this.boardTemplate.toDto(),
        this.scoringTags.map { it.toDto() },
        this.areas.map { it.rectangle },
        this.numberOfRoundsPlayed
    )
