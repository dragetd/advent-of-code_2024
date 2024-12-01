package net.speciesm.draget.aoc2024.utils.aoc

import net.speciesm.draget.aoc2024.utils.XDGUtils
import java.io.File
import java.time.LocalDate

class AoCInputRepository(private val year: Int) {
    private val inputsPath: String = "${XDGUtils.getConfigHome("draget_aoc")}/$year/inputs/"

    init {
        File(inputsPath).mkdirs()
    }

    fun findByDay(day: Int, session: String): String {
        val inputFile = File("$inputsPath/day$day.txt")
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
