package kpdev.rustlingleaves.model.skeleton

import jakarta.persistence.*
import java.util.*

@Entity
class Field(
    @Embedded
    val point: Point,

    @Enumerated(EnumType.STRING)
    val type: FieldType,

    var isTicked: Boolean = false,
    var isIncluded: Boolean = false,
    var inclusionRoundId: Int = -1,

    @Id
    val id: UUID = UUID.randomUUID(),
) {
    fun include(roundId: Int) {
        isIncluded = true
        inclusionRoundId = roundId
    }

    fun tick(roundId: Int) {
        isIncluded = true
        isTicked = true
        inclusionRoundId = roundId
    }
}