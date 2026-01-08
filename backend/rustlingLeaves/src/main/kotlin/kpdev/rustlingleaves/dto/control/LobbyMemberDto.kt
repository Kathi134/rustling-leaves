package kpdev.rustlingleaves.dto.control


import kpdev.rustlingleaves.model.control.LobbyMember
import java.util.*

data class LobbyMemberDto (
    val id: UUID,
    val name: String,
    val playerId: UUID?
)

fun LobbyMember.toDto(): LobbyMemberDto =
    LobbyMemberDto(this.id, this.name, this.playerObject?.id)