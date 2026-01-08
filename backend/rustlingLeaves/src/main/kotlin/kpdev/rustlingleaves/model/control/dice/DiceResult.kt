package kpdev.rustlingleaves.model.control.dice

enum class DiceResult(val value: Int, val cloud: Boolean = false) {
    OPTION_1(1),
    OPTION_2(2),
    OPTION_3(3),
    OPTION_4(4),
    OPTION_CLOUDED_2(2, true),
    OPTION_CLOUDED_3(3, true);

    companion object {
        val CLOUD_VALUES = DiceResult.entries.filter { it.cloud }.toSet()
        val NONE_CLOUD_VALUES = DiceResult.entries.filter { !it.cloud }.toSet()
    }
}