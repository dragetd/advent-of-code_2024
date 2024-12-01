package net.speciesm.draget.aoc2024.utils

const val SUMMARY_LENGTH = 10

fun String.ellipse(): String {
    val lines = this.lines()
    return when {
        lines.size > 1 -> lines.first().take(SUMMARY_LENGTH - 1) + "…"
        lines.first().length > SUMMARY_LENGTH -> lines.first().take(SUMMARY_LENGTH - 1) + "…"
        else -> lines.first()
    }
}
