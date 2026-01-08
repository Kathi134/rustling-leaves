package kpdev.rustlingleaves.dto.control

import kpdev.rustlingleaves.dto.skeleton.PlayerCardDtoResponse
import kpdev.rustlingleaves.dto.skeleton.toDto
import kpdev.rustlingleaves.model.control.Player
import java.util.*

data class PlayerDto (
    val id: UUID,
    val stopped: Boolean,
    val playerCard: PlayerCardDtoResponse,
)

fun Player.toDto(): PlayerDto =
    PlayerDto(id, hasStopped, playerCard.toDto())