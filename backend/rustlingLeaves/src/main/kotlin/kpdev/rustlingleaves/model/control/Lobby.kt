@file:OptIn(ExperimentalStdlibApi::class)

package kpdev.rustlingleaves.model.control

import jakarta.persistence.*
import kpdev.rustlingleaves.model.config.GameConfig
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

@Entity
class Lobby(
    @Id
    val id: UUID = UUID.randomUUID(),

    var name: String,

    val lobbyCode: String = Random.nextInt().toHexString(),

    val createdAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "lobby_id")
    val members: MutableList<LobbyMember> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    var state: LobbyState = LobbyState.IN_CONFIG,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "game_engine_id")
    var game: GameEngine? = null
) {
    val isFinished: Boolean
        get() = game?.isFinished ?: false
    // TODO: update state

    fun isConfigValid(config: List<GameConfig>): Boolean =
        config.size == members.size

    fun startGame(config: List<GameConfig>) : GameEngine {
        state = LobbyState.RUNNING_GAME
        config.forEach { it.member.initPlayer(it) }
        game = GameEngine(config)
        members.forEach { it.playerObject?.gameEngine = game }
        return game!!
    }

    fun addMember(member: LobbyMember) =
        members.add(member)

    fun checkLobbyPasscode(code: String) =
        code == lobbyCode

}