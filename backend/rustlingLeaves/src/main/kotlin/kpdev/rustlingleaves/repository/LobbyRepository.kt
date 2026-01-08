package kpdev.rustlingleaves.repository

import kpdev.rustlingleaves.model.control.Lobby
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LobbyRepository : JpaRepository<Lobby, UUID> {
    fun findByName(name: String): List<Lobby>
}