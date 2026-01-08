package kpdev.rustlingleaves.model.move.drawing

import jakarta.persistence.*
import kpdev.rustlingleaves.model.skeleton.BoardTemplate
import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.Point
import kpdev.rustlingleaves.model.skeleton.River
import java.util.*

@Entity
class Area(
    topLeft: Point, bottomRight: Point,

    @Embedded
    val rectangle: Rectangle = Rectangle(topLeft, bottomRight),

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_template_id", nullable = false)
    val boardTemplate: BoardTemplate, // TODO remove and do bT[area].fieldTypes

    @Id
    val id: UUID = UUID.randomUUID(),
) {
    val points : Set<Point>
        get() = rectangle.points

    val fieldTypes : Set<FieldType>
        get() = boardTemplate.getFieldTypes(points)

    fun crossesRiver(river: River) : Boolean =
        river.overlapsWithArea(this)

    fun isAdjacentToArea(area: Area) : Boolean =
        rectangle.isAdjacentToRectangle(area.rectangle)

    fun overlapsWithArea(area: Area) : Boolean =
        rectangle.overlapsWithArea(area.rectangle)

    fun numberFieldsOfType(fieldType: FieldType) : Int =
        this[fieldType].size

    fun includes(point: Point): Boolean =
        rectangle.includes(point)

    operator fun get(tickedFieldType: FieldType) : Set<Point> =
        boardTemplate.getPointsOfFieldTypeInRectangle(rectangle, tickedFieldType)
}