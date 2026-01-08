package kpdev.rustlingleaves.repository

import kpdev.rustlingleaves.model.control.LobbyMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LobbyMemberRepository : JpaRepository<LobbyMember, UUID>