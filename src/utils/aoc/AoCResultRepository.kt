package net.speciesm.draget.aoc2024.utils.aoc

import net.speciesm.draget.aoc2024.utils.XDGUtils
import net.speciesm.draget.aoc2024.utils.aoc.AoCWeb
import java.io.File
import java.time.LocalDate

class AoCResultRepository(private val year: Int) {
    private val resultsPath: String = "${XDGUtils.getConfigHome("draget_aoc")}/$year/results/"

    init {
        File(resultsPath).mkdirs()
    }

    fun findByDay(day: Int, session: String): String {
        val inputFile = File("$resultsPath/day$day.txt")
        if (LocalDate.of(year, 12, day).isAfter(LocalDate.now())) {
            throw IllegalArgumentException("Requested day $day is after today, not downloading any input.")
        }

        if (inputFile.exists()) {
            return inputFile.readText()
        }

        println("Input for day $day not yet cached, downloading.")
        val input = AoCWeb.downloadInput(year = 2024, day = day, session = session)
        if (input.contains("Please don't repeatedly request this endpoint before it unlocks!")) {
            throw IllegalArgumentException("Server did not return the input.")
        }

        inputFile.writeText(input)
        return input
    }
}
