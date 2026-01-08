package kpdev.rustlingleaves.dto.skeleton

import kpdev.rustlingleaves.model.skeleton.Field
import kpdev.rustlingleaves.model.skeleton.Point

data class FieldDto(
    val point: Point,
    val type: FieldTypeDto,
    val isTicked: Boolean,
    val isIncluded: Boolean
)

fun Field.toDto(): FieldDto =
    FieldDto(point, type.toDto(), isTicked, isIncluded)