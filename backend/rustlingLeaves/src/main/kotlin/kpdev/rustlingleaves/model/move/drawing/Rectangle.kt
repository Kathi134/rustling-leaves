package kpdev.rustlingleaves.model.move.drawing

import jakarta.persistence.*
import kpdev.rustlingleaves.model.skeleton.Point

@Embeddable
class Rectangle(
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "x", column = Column(name = "top_left_x")),
        AttributeOverride(name = "y", column = Column(name = "top_left_y"))
    )
    val topLeft: Point,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "x", column = Column(name = "bottom_right_x")),
        AttributeOverride(name = "y", column = Column(name = "bottom_right_y"))
    )
    val bottomRight: Point,
)
{
    // only choose if you need the field types
    val points : Set<Point>
        get() {
            val set = mutableSetOf<Point>()
            for (x in topLeft.x..bottomRight.x)
            {
                for(y in topLeft.y..bottomRight.y)
                {
                    set.add(Point(x, y))
                }
            }
            return set
        }

    private val allCornerPoints : Set<Point>
        get() =
            setOf(topLeft, bottomRight, Point(bottomRight.x, topLeft.y),  Point(topLeft.x, bottomRight.y))


    fun isAdjacentToRectangle(rectangle: Rectangle) : Boolean {
        // x is adjacent
        if(topLeft.x == rectangle.bottomRight.x + 1
            || bottomRight.x == rectangle.topLeft.x - 1)
            if(rectangle.topLeft.y in topLeft.y..bottomRight.y
                || rectangle.bottomRight.y in topLeft.y..bottomRight.y)
                return true
        if(topLeft.y == rectangle.bottomRight.y + 1
           || bottomRight.y == rectangle.topLeft.y - 1)
            if(rectangle.topLeft.x in topLeft.x..bottomRight.x
                || rectangle.bottomRight.x in topLeft.x..bottomRight.x)
                return true
        return false
    }

    fun overlapsWithArea(rectangle: Rectangle) : Boolean =
        allCornerPoints.any { rectangle.includes(it) }
            || rectangle.allCornerPoints.any { includes(it) }


    fun includes(point: Point) : Boolean =
        point.y in topLeft.y..bottomRight.y
            && point.x in topLeft.x..bottomRight.x

    fun includes(rectangle: Rectangle) : Boolean =
        includes(rectangle.topLeft) && includes(rectangle.bottomRight)
}