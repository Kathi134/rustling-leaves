package kpdev.rustlingleaves.repository

import jakarta.persistence.LockModeType
import kpdev.rustlingleaves.model.control.GameEngine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface GameEngineRepository : JpaRepository<GameEngine, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select g from GameEngine g where g.id = :id")
    fun findByIdForUpdate(id: UUID): GameEngine

}
