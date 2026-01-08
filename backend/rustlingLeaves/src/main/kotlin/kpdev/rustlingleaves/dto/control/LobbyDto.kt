package kpdev.rustlingleaves.dto.control

import kpdev.rustlingleaves.model.control.Lobby
import kpdev.rustlingleaves.model.control.LobbyState
import java.util.UUID

data class LobbyDto(
    val id: UUID,
    val name: String,
    val members: List<LobbyMemberDto>,
    val code: String,
    val state: LobbyState,
    val gameId: UUID?
)

// TODO(use for list get)
data class SecretLobbyDto(
    val id: UUID,
    val name: String
)


fun Lobby.toDto() = LobbyDto(
    id = this.id,
    name = this.name,
    members = this.members.map { it.toDto() },
    code = this.lobbyCode,
    state = this.state,
    gameId = this.game?.id,
)

fun Lobby.toSecretDto() = SecretLobbyDto(id = id, name = name)