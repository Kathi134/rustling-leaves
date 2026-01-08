package kpdev.rustlingleaves.model.skeleton

import jakarta.persistence.*
import kpdev.rustlingleaves.model.config.BoardTemplateCollection
import kpdev.rustlingleaves.model.config.Season
import kpdev.rustlingleaves.model.move.drawing.Area
import kpdev.rustlingleaves.model.move.drawing.Rectangle
import java.util.*

@Entity
class BoardTemplate(
    season: Season,

    @Id
    val id: UUID = UUID.randomUUID(),

    private val sizeX: Int = 10,
    private val sizeY: Int = 10,

    @Embedded
    val fields: Matrix<Field> = BoardTemplateCollection.getDefaultFieldTypesForSeason(season),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "river_id")
    val river: River = BoardTemplateCollection.getDefaultRiver()
) {

    fun getFieldTypes(points: Set<Point>): Set<FieldType> =
        points.map { fields[it].type }.toSet()

    fun getPointsOfFieldTypeInRectangle(rectangle: Rectangle, targetType: FieldType) : Set<Point> =
        rectangle.points.filter { fields[it].type == targetType }.toSet()

    // set all fields in area as included
    fun drawArea(area: Area, roundId: Int): Map<FieldType, Int> {
        val affectedFields = fields[area]
        affectedFields.forEach { it.include(roundId) }
        return countAffectedFieldsGrouped(affectedFields)
    }

    fun tickFieldTypeInArea(area: Area, tickedFieldType: FieldType, roundId: Int): Map<FieldType, Int>  {
        val affectedFields = fields[area]
            .filter { it.type == tickedFieldType || it.type in tickedFieldType.tickGroup }
        affectedFields.forEach { it.tick(roundId) }
        return countAffectedFieldsGrouped(affectedFields)
    }

    private fun countAffectedFieldsGrouped(fields: Collection<Field>) : Map<FieldType, Int> =
        fields.groupingBy {it.type}.eachCount()

}


