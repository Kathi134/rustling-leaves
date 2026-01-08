package kpdev.rustlingleaves.dto.skeleton

import kpdev.rustlingleaves.model.skeleton.ScoringTag

data class ScoringTagDto (
    val fieldTypeDto: FieldTypeDto,
    val max: Int,
    val value: Int
)

fun ScoringTag.toDto(): ScoringTagDto =
    ScoringTagDto(fieldType.toDto(), max, value)