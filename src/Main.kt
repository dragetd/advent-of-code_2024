package net.speciesm.draget.aoc2024

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import net.speciesm.draget.aoc2024.utils.SolutionRepository
import net.speciesm.draget.aoc2024.utils.aoc.AoCInputRepository
import net.speciesm.draget.aoc2024.utils.aoc.AoCSession

private const val YEAR: Int = 2024

fun main(args: Array<String>) = Main().main(args)

class Main : CliktCommand() {
    val interactiveLogin: Boolean by option("-i", "--interactive-login", help = "Start interactive login and store the AoC session.").flag()
    val minDay: Int by option("-m", "--min-day", help = "Start solving only from this day onwards.").int().default(1)

    override fun run() {
        val session = AoCSession.getSession(YEAR, interactiveLogin)

        for (day in minDay..24) {
            val input = try {
                AoCInputRepository(YEAR).findByDay(day, session)
            } catch (e: Exception) {
                println(e.message)
                continue
            }
            val solution: DaySolution = try {
                SolutionRepository.findByDay(YEAR, day)
            } catch (_: ClassNotFoundException) {
                println("Day$day: Not yet implemented.")
                continue
            }

            runCatching { solution.solve1(input) }
                .onSuccess { solution1: String -> println("Day$day: Solution1 = $solution1") }
                .onFailure { println("Day$day: part 1 is not yet implemented.") }

            runCatching { solution.solve2(input) }
                .onSuccess { solution2: String -> println("Day$day: Solution2 = $solution2") }
                .onFailure { println("Day$day: part 2 is not yet implemented.") }
        }
    }
}
