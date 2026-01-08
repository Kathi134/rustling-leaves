package kpdev.rustlingleaves.service

import kpdev.rustlingleaves.model.control.Player
import kpdev.rustlingleaves.repository.PlayerRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
) {
    fun getPlayer(playerId: UUID): Player =
        playerRepository.findById(playerId).get()
}
