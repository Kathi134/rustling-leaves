package kpdev.rustlingleaves.service

import kpdev.rustlingleaves.dto.move.QuitGameDtoRequest
import kpdev.rustlingleaves.dto.move.StoreMoveDtoRequest
import kpdev.rustlingleaves.dto.skeleton.asEntity
import kpdev.rustlingleaves.model.control.GameEngine
import kpdev.rustlingleaves.model.control.Player
import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.control.dice.PairedDiceResult
import kpdev.rustlingleaves.model.move.drawing.Area
import kpdev.rustlingleaves.model.move.drawing.Rectangle
import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.PlayerCard
import kpdev.rustlingleaves.model.skeleton.Point
import kpdev.rustlingleaves.repository.GameEngineRepository
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameEngineService(
    val gameEngineRepository: GameEngineRepository,
    private val playerService: PlayerService,
    private val socket: SimpMessagingTemplate
) {
    // clients only register for this socket after they stored their own move
    fun cardsSocketDest(gameId: UUID, roundId: Int): String =
        "/topic/game/$gameId/$roundId/cards"

    fun scoresSocketDest(gameId: UUID): String =
        "/topic/game/$gameId/scores"

    fun diceSocketDest(gameId: UUID): String =
        "/topic/game/$gameId/dice"

    fun getGameEngine(id: UUID) : GameEngine =
        gameEngineRepository.getReferenceById(id)

    fun rollDiceForRound(id: UUID, roundId: Int) : PairedDiceResult {
        val ge = getGameEngine(id)
        val res = ge.rollDice()
        gameEngineRepository.save(ge)
        socket.convertAndSend(diceSocketDest(ge.id), res)
        return res
    }

    private fun getAreaFromPlayerAndRectangle(player: Player, rectangle: Rectangle) : Area =
        Area(topLeft = rectangle.topLeft, bottomRight = rectangle.bottomRight, boardTemplate = player.playerCard.boardTemplate)

    fun checkIfAreaIsValid(id: UUID, playerId: UUID, rectangle: Rectangle) : Any {
        val player = playerService.getPlayer(playerId)
        val area = getAreaFromPlayerAndRectangle(player, rectangle)
        val ge = getGameEngine(id)
        return if(ge.isDrawingAreaAllowed(player, area))
            true
        else
            ge.getReasonForDrawingAreaUnallowed(player, area)
    }

    fun getTickableFieldTypesOnArea(id: UUID, playerId: UUID, rectangle: Rectangle): Set<FieldType> {
        val player = playerService.getPlayer(playerId)
        val area = getAreaFromPlayerAndRectangle(player, rectangle)
        return getGameEngine(id).getTickableFieldTypesInArea(area)
    }

    fun getFieldsOfTypeInRectangle(id: UUID, playerId: UUID, rectangle: Rectangle, type: FieldType): Set<Point> {
        val player = playerService.getPlayer(playerId)
        return getGameEngine(id).getPointsOfFieldTypeInRectangle(player, type, rectangle)
    }

    fun storeMoveForPlayer(id: UUID, playerId: UUID, moveDtoRequest: StoreMoveDtoRequest) : PlayerCard {
        val ge = getGameEngine(id)
        val player = playerService.getPlayer(playerId)
        val area = getAreaFromPlayerAndRectangle(player, moveDtoRequest.rectangle)

        val resultingCard = ge.storeMove(player, area, moveDtoRequest.type.asEntity(), moveDtoRequest.roundId)
        gameEngineRepository.save(ge)

        socket.convertAndSend(cardsSocketDest(ge.id, moveDtoRequest.roundId), resultingCard)

        return resultingCard
    }

    fun quitGameForPlayer(id: UUID, playerId: UUID, quitDtoRequest: QuitGameDtoRequest): PlayerCard {
        val ge = getGameEngine(id)
        val player = playerService.getPlayer(playerId)

        val resultingCard = ge.stopGameForPlayer(player, quitDtoRequest.roundId)
        gameEngineRepository.save(ge)

        socket.convertAndSend(cardsSocketDest(ge.id, quitDtoRequest.roundId), resultingCard)
        performEndActionsIfRequired(ge)

        return resultingCard
    }

    private fun performEndActionsIfRequired(game: GameEngine) {
        if(!game.isFinished)
            return
        socket.convertAndSend(scoresSocketDest(game.id), game.finalScoring)
    }

    fun getPlayerCardDetails(id: UUID, playerId: UUID): PlayerCard =
        playerService.getPlayer(playerId).playerCard

}
