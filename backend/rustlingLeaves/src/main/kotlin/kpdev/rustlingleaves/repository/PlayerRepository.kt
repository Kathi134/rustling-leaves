package kpdev.rustlingleaves.repository

import kpdev.rustlingleaves.model.control.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PlayerRepository : JpaRepository<Player, UUID>