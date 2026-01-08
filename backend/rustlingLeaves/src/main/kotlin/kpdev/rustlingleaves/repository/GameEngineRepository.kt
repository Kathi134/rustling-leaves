package kpdev.rustlingleaves.repository

import kpdev.rustlingleaves.model.control.GameEngine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameEngineRepository : JpaRepository<GameEngine, UUID>
