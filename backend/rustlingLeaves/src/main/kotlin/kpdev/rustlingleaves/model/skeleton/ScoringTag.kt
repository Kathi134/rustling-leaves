package kpdev.rustlingleaves.model.skeleton

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.util.*

@Entity
class ScoringTag(
    @Enumerated(EnumType.STRING)
    val fieldType: FieldType,
    val max: Int,

    @Id
    val id: UUID = UUID.randomUUID(),
) {
    var value: Int = 0
        private set

    var lastTickedRoundIndex: Int = -1
        private set

    fun add(value: Int, currentRoundId: Int) : Boolean {
        val preview = this.value + value
        if(preview > max)
            return false
        this.value = preview
        lastTickedRoundIndex = currentRoundId
        return true
    }

    fun inc(currentRoundId: Int) : Boolean =
        add(1, currentRoundId)
}

operator fun Set<ScoringTag>.get(tickedFieldType: FieldType) : ScoringTag {
    return try {
        this.first { it.fieldType == tickedFieldType }
    } catch (_: NoSuchElementException) {
        this.first { it.fieldType in tickedFieldType.tickGroup }
    }
}
