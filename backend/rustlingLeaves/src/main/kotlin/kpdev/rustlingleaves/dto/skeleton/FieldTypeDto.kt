package kpdev.rustlingleaves.dto.skeleton

import kpdev.rustlingleaves.model.skeleton.FieldType

data class FieldTypeDto(
    val enumName: String
)

fun FieldType.toDto() = FieldTypeDto(this.name)

fun FieldTypeDto.asEntity() = FieldType.valueOf(this.enumName)