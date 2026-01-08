package kpdev.rustlingleaves.dto.skeleton

import kpdev.rustlingleaves.model.skeleton.BoardTemplate
import kpdev.rustlingleaves.model.skeleton.River

data class BoardTemplateDto (
    val fields: List<List<FieldDto>>,
    val river: River
)

fun BoardTemplate.toDto(): BoardTemplateDto =
    BoardTemplateDto(fields.getAs2DList().map { it.map { inner -> inner.toDto() } }, river)
