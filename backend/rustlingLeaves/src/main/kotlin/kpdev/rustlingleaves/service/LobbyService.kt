package kpdev.rustlingleaves.service

import kpdev.rustlingleaves.dto.control.toDto
import kpdev.rustlingleaves.model.config.GameConfig
import kpdev.rustlingleaves.model.control.Lobby
import kpdev.rustlingleaves.model.control.LobbyMember
import kpdev.rustlingleaves.model.control.LobbyState
import kpdev.rustlingleaves.repository.GameEngineRepository
import kpdev.rustlingleaves.repository.LobbyMemberRepository
import kpdev.rustlingleaves.repository.LobbyRepository
import kpdev.rustlingleaves.repository.PlayerRepository
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class LobbyService(
    private val lobbyRepository: LobbyRepository,
    private val playerRepository: PlayerRepository,
    private val memberRepository: LobbyMemberRepository,
    private val messagingTemplate: SimpMessagingTemplate,
    private val gameEngineRepository: GameEngineRepository
) {

    fun getAllLobbies(): List<Lobby> =
        lobbyRepository.findAll()

    fun createLobby(lobbyName: String): Lobby {
        val lobby = Lobby(name = lobbyName)
        return lobbyRepository.save(lobby)
    }

    fun getLobby(id: UUID): Lobby =
        lobbyRepository.findById(id)
            .orElseThrow { RuntimeException("Lobby not found") }

    fun getLobby(name: String): List<Lobby> =
        lobbyRepository.findByName(name)

    fun getMember(memberId: UUID): LobbyMember =
        memberRepository.findById(memberId)
            .orElseThrow { RuntimeException("Member not found") }

    fun joinLobby(lobbyId: UUID, lobbyCode: String, userName: String): Pair<Lobby, LobbyMember> {
        var lobby = getLobby(lobbyId)
        if(!lobby.checkLobbyPasscode(lobbyCode))
            throw IllegalAccessException("Lobby code incorrect.")

        val member = LobbyMember(name = userName)
        lobby.addMember(member)
        lobby = lobbyRepository.save(lobby)

        messagingTemplate.convertAndSend("/topic/lobby/${lobby.id}", lobby.toDto())
        return Pair(lobby, member)
    }

    fun leaveLobby(lobbyId: UUID, memberId: UUID) {
        val lobby = getLobby(lobbyId)
        val member = memberRepository.findById(memberId)
            .orElseThrow { RuntimeException("Member not found") }

        memberRepository.delete(member)
        lobby.members.removeIf { it.id == memberId }

        // Delete lobby if empty
        if (lobby.members.isEmpty()) {
            lobbyRepository.delete(lobby)
        }
        else {
            // Broadcast updated lobby
            messagingTemplate.convertAndSend("/topic/lobby/${lobby.id}", lobby.toDto())
        }
    }

    fun startLobby(id: UUID, config: List<GameConfig>): Lobby {
        val lobby = lobbyRepository.findById(id).orElseThrow { RuntimeException("Lobby not found") }

        if (lobby.game != null || lobby.state == LobbyState.RUNNING_GAME) {
            throw RuntimeException("Game already started")
        }

        lobby.startGame(config)
        val savedLobby = lobbyRepository.save(lobby)

        messagingTemplate.convertAndSend("/topic/lobby/$id", savedLobby.toDto())
        return savedLobby
    }
}