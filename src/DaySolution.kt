package net.speciesm.draget.aoc2024

import net.speciesm.draget.aoc2024.utils.SolutionRepository
import net.speciesm.draget.aoc2024.utils.ellipse

interface DaySolution {
    fun String.printSolve1() {
        println("Solve1 on input \"${this.ellipse()}\": ${solve1(this)}")
    }

    fun String.printSolve2() {
        println("Solve2 on input \"${this.ellipse()}\": ${solve2(this)}")
    }

    fun solve1(input: String): String
    fun solve2(input: String): String

    fun getDay(): Int {
        return SolutionRepository.dayByClass(this.javaClass)
    }
}
