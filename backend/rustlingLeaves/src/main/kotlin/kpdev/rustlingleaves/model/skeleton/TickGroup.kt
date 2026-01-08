package kpdev.rustlingleaves.model.skeleton

class TickGroup(
    private vararg val otherTypes: FieldType
) {
    operator fun contains(type: FieldType): Boolean {
        return otherTypes.any { it == type }
    }
}
