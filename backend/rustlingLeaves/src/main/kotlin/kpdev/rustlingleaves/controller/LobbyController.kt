package kpdev.rustlingleaves.controller

import kpdev.rustlingleaves.dto.*
import kpdev.rustlingleaves.dto.config.GameConfigDtoRequest
import kpdev.rustlingleaves.dto.config.asEntity
import kpdev.rustlingleaves.dto.control.JoinLobbyRequest
import kpdev.rustlingleaves.dto.control.LobbyDto
import kpdev.rustlingleaves.dto.control.LobbyJoinResponse
import kpdev.rustlingleaves.dto.control.toDto
import kpdev.rustlingleaves.service.LobbyService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/lobbies")
class LobbyController(
    private val lobbyService: LobbyService,
) {
    @GetMapping
    fun listLobbies(): List<LobbyDto> =
        lobbyService.getAllLobbies().map { it.toDto() }

    @PostMapping
    fun createLobby(@RequestBody nameDto: SimpleStringDto): LobbyDto =
        lobbyService.createLobby(nameDto.content).toDto()

    @GetMapping("/{id}")
    fun getLobby(@PathVariable id: UUID): LobbyDto =
        lobbyService.getLobby(id).toDto()

//    @PutMapping("/{id}/name")
//    fun editLobbyName(@PathVariable id: UUID, @RequestBody name: String): LobbyDto =
//        lobbyService.updateLobbyName(id, name).toDto()

    @PostMapping("/{id}/members")
    fun joinLobby(@PathVariable id: UUID, @RequestBody joinLobbyRequest: JoinLobbyRequest): LobbyJoinResponse {
        val (lobby, me) = try {
            lobbyService.joinLobby(id, joinLobbyRequest.lobbyCode, joinLobbyRequest.playerName)
        } catch (e: IllegalAccessException) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, e.message)
        }
        return LobbyJoinResponse(lobby.toDto(), me.toDto())
    }

    @DeleteMapping("/{id}/members/{memberId}")
    fun leaveLobby(@PathVariable id: UUID, @PathVariable memberId: UUID) =
        lobbyService.leaveLobby(id, memberId)

//    @PutMapping("/{lobbyId}/members/{memberId}/name")
//    fun editMemberName(@PathVariable lobbyId: UUID, @PathVariable memberId: UUID, @RequestBody name: String) : LobbyDto =
//        lobbyService.updateLobbyMemberName(lobbyId, memberId, name).toDto()

    @PutMapping("/{id}/status")
    fun startLobby(@PathVariable id: UUID, @RequestBody config: List<GameConfigDtoRequest>): LobbyDto =
        lobbyService.startLobby(id, config.map { it.asEntity(lobbyService) }).toDto()

}
