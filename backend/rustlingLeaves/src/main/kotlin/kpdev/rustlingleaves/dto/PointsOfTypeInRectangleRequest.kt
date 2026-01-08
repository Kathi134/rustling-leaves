package kpdev.rustlingleaves.dto

import kpdev.rustlingleaves.dto.skeleton.FieldTypeDto
import kpdev.rustlingleaves.model.move.drawing.Rectangle

data class PointsOfTypeInRectangleRequest(
    val type: FieldTypeDto,
    val rectangle: Rectangle,
)
