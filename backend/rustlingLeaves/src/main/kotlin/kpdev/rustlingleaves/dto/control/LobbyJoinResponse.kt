package kpdev.rustlingleaves.dto.control

data class LobbyJoinResponse(
    val lobby: LobbyDto,
    val me: LobbyMemberDto
)
