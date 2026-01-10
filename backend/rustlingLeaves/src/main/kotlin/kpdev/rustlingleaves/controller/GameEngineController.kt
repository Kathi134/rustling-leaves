package kpdev.rustlingleaves.controller

import kpdev.rustlingleaves.dto.PointsOfTypeInRectangleRequest
import kpdev.rustlingleaves.dto.move.QuitGameDtoRequest
import kpdev.rustlingleaves.dto.move.StoreMoveDtoRequest
import kpdev.rustlingleaves.dto.skeleton.*
import kpdev.rustlingleaves.model.control.dice.DiceResult
import kpdev.rustlingleaves.model.control.dice.PairedDiceResult
import kpdev.rustlingleaves.model.move.drawing.Area
import kpdev.rustlingleaves.model.move.drawing.Rectangle
import kpdev.rustlingleaves.model.skeleton.Point
import kpdev.rustlingleaves.service.GameEngineService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/games")
class GameEngineController(
    private val gameEngineService: GameEngineService,
) {
    @PutMapping("/{id}/rounds/{roundId}/dice")
    fun rollDice(@PathVariable id: UUID, @PathVariable roundId: Int) : PairedDiceResult =
        gameEngineService.rollDiceForRound(id, roundId)

    @GetMapping("{id}/rounds")
    fun getCurrentRound(@PathVariable id: UUID) : Int =
        gameEngineService.getCurrentRound(id)

    @GetMapping("/{id}/players/{playerId}/card")
    fun getPlayerCardDetails(@PathVariable id: UUID, @PathVariable playerId : UUID) : PlayerCardDetailedDtoResponse =
        gameEngineService.getPlayerCardDetails(id, playerId).toDetailedDto()

    @PostMapping("/{id}/players/{playerId}/areas/valid")
    fun checkIfAreaIsValid(@PathVariable id: UUID, @PathVariable playerId : UUID, @RequestBody rect: Rectangle) : Any =
        gameEngineService.checkIfAreaIsValid(id, playerId, rect)

    @PostMapping("/{id}/players/{playerId}/areas/tickable-types")
    fun getTickableFieldTypesOnArea(@PathVariable id: UUID,  @PathVariable playerId : UUID, @RequestBody rect: Rectangle) : List<FieldTypeDto> =
        gameEngineService.getTickableFieldTypesOnArea(id, playerId, rect).map { it.toDto() }

    @PostMapping("/{id}/players/{playerId}/areas/points-of-type")
    fun getPointsOfTypeOnArea(@PathVariable id: UUID, @PathVariable playerId: UUID, @RequestBody req: PointsOfTypeInRectangleRequest) : Set<Point> =
        gameEngineService.getFieldsOfTypeInRectangle(id, playerId, req.rectangle, req.type.asEntity())

    @PostMapping("/{id}/players/{playerId}/moves")
    fun storeMoveForPlayer(
        @PathVariable id: UUID, @PathVariable playerId: UUID,
        @RequestBody moveDtoRequest: StoreMoveDtoRequest
    ): PlayerCardDetailedDtoResponse =
        gameEngineService.storeMoveForPlayer(id, playerId, moveDtoRequest).toDetailedDto()

    @PostMapping("/{id}/players/{playerId}/stop")
    fun quitGameForPlayer(
        @PathVariable id: UUID, @PathVariable playerId: UUID,
        @RequestBody quitDtoRequest: QuitGameDtoRequest
    ) : PlayerCardDtoResponse =
        gameEngineService.quitGameForPlayer(id, playerId, quitDtoRequest).toDto()

}