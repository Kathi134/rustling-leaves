package kpdev.rustlingleaves.model.config

import kpdev.rustlingleaves.model.skeleton.FieldType
import kpdev.rustlingleaves.model.skeleton.Matrix
import java.io.File

class BoardConfigParser {
    companion object {
        private val cache: MutableMap<Season, Matrix<FieldType>> = mutableMapOf()

        fun parseConfig(season: Season): Matrix<FieldType> =
            cache.computeIfAbsent(season) {
                val path = "./assets/defaultBoardTemplates"
                val file = "${season.toString().lowercase()}-default-board"
                val lines = File("$path/$file").bufferedReader().readLines()
                val alphabet = parseAlphabet(lines[0])
                val arr = lines.drop(1).map { parseLine(it, alphabet) }.toList()
                Matrix(arr)
            }

        private fun parseAlphabet(line: String): Map<String, FieldType> =
            line.split(",")
                .associate { v ->
                    val (key, value) = v.split(':')
                    key.trim() to FieldType.valueOf(value.trim())
                }

        private fun parseLine(line: String, alphabet: Map<String, FieldType>): List<FieldType> =
            line.split(",").map { alphabet.getValue(it) }.toList()

    }
}