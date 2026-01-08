package kpdev.rustlingleaves.model.control

import jakarta.persistence.*
import kpdev.rustlingleaves.model.config.BoardTemplateCollection
import kpdev.rustlingleaves.model.config.GameConfig
import kpdev.rustlingleaves.model.skeleton.PlayerCard
import java.util.*

@Entity
class LobbyMember (
    val name: String,

    @Id
    val id: UUID = UUID.randomUUID(),

    @OneToOne(mappedBy = "lobbyMember")
    var playerObject: Player? = null
) {
    fun initPlayer(config: GameConfig) : Player {
        val card = PlayerCard(config)
        val diceEvents = BoardTemplateCollection.getDefaultDiceEvents(config.season)
        val player = Player(
            playerCard = card,
            diceEventIdentifiers = diceEvents.map { DiceEventIdentifier.fromDiceEvent(it) }.toMutableList(),
            lobbyMember = this
        )
        playerObject = player
        return player
    }
}