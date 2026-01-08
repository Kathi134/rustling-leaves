package kpdev.rustlingleaves.dto.move

import kpdev.rustlingleaves.dto.skeleton.FieldTypeDto
import kpdev.rustlingleaves.model.move.drawing.Area
import kpdev.rustlingleaves.model.move.drawing.Rectangle

data class StoreMoveDtoRequest (
    val roundId: Int,
    val rectangle: Rectangle,
    val type: FieldTypeDto
)

