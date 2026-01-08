package kpdev.rustlingleaves.model.skeleton

import jakarta.persistence.*
import kpdev.rustlingleaves.model.move.drawing.Area

@Embeddable
open class Matrix<T>(
    private val dimY: Int,
    private val dimX: Int,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "board_template_id")
    private val flatValueStream: MutableList<T> = mutableListOf(),
) {
    constructor(values: Collection<Collection<T>>) : this(values.size, values.first().size) {
        require(values.size * values.first().size == dimX * dimY) { "Field count does not match dimensions" }
        flatValueStream.addAll(values.flatten())
    }

    operator fun get(point: Point): T {
        require(point.x in 0 until dimX) { "x=${point.x} out of bounds" }
        require(point.y in 0 until dimY) { "y=${point.y} out of bounds" }
        return flatValueStream[(point.y) * dimY + point.x]
    }

    operator fun get(area: Area): Set<T> =
        area.points.map { this[it] }.toSet()

    operator fun set(point : Point, value: T) {
        require(point.x in 0 until dimX) { "x=${point.x} out of bounds" }
        require(point.y in 0 until dimY) { "y=${point.y} out of bounds" }
        this[point] = value
    }

    private fun convertFlatIndexToPoint(flatIndex: Int): Point =
        Point(flatIndex%dimY, flatIndex/dimY)


    fun <R> mapToType(mapper: (T) -> R) : Matrix<R> {
        val newFlat = flatValueStream.map(mapper).toMutableList()
        return Matrix(dimY, dimX, newFlat)
    }

    fun <R> mapIndexedToType(mapper: (Point, T) -> R) : Matrix<R> {
        val newFlat = flatValueStream.mapIndexed { index, t -> mapper(convertFlatIndexToPoint(index), t) }.toMutableList()
        return Matrix(dimY, dimX, newFlat)
    }

    fun getAs2DList() : List<List<T>> =
        (0 until dimY).map { y ->
            (0 until dimX).map { x ->
                this[Point(x,y)]
            }
        }
}

//@Embeddable
//open class FieldMatrix(
//    private val dimY: Int,
//    private val dimX: Int,
//
//    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
//    @JoinColumn(name = "board_template_id")
//    private val flatValueStream: MutableList<Field>
//) {
//    operator fun get(point: Point): Field =
//        flatValueStream[(point.y) * dimY + point.x]
//
//    operator fun get(area: Area): Set<Field> =
//        area.points.map { this[it] }.toSet()
//
//    operator fun set(point : Point, value: Field) {
//        this[point] = value
//    }
//}

class FieldMatrix(dimY: Int, dimX: Int, flatValueStream: MutableList<Field>)
    : Matrix<Field>(dimY, dimX, flatValueStream)




// 0,1,2,3,4,5
// 0,1,2,3,4,5  // [1][2] -> 8; 8/6=1, 8%6=2
// 0,1,2,3,4,5