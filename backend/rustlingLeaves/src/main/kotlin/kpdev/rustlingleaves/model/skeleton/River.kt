package kpdev.rustlingleaves.model.skeleton

import jakarta.persistence.*
import kpdev.rustlingleaves.model.move.drawing.Area
import kpdev.rustlingleaves.model.move.drawing.Rectangle
import java.util.*

@Entity
class River(
    definingRectangles : Set<Rectangle>,

    @Id
    val id: UUID = UUID.randomUUID(),

    @ElementCollection
    @CollectionTable(name = "river_rectangles", joinColumns = [JoinColumn(name = "river_id")])
    var pairedRectangles: Set<Rectangle> = setOf()
) {
    init {
        pairedRectangles = definingRectangles.flatMap { rect ->
            if(rect.bottomRight.y - rect.topLeft.y == 1) {
                (rect.topLeft.x until rect.bottomRight.x + 1)
                    .map { x -> Rectangle(Point(x, rect.topLeft.y), Point(x, rect.bottomRight.y)) }
            } else {
                (rect.topLeft.y until rect.bottomRight.y + 1)
                    .map { y -> Rectangle(Point(rect.topLeft.x, y), Point(rect.bottomRight.x, y))}
            }
        }.toSet()
    }

    fun overlapsWithArea(area: Area) : Boolean =
        pairedRectangles.any { area.rectangle.includes(it) }

}
