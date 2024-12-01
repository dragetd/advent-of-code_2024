package net.speciesm.draget.aoc2024.utils

import net.speciesm.draget.aoc2024.DaySolution

object SolutionRepository {
//    fun findAllSolutions(): List<DaySolution> {
//        val reflections = Reflections("net.speciesm.draget.aoc2024.solutions")
//        val dayClasses = reflections.getSubTypesOf(DaySolution::class.java)
//        return dayClasses.mapNotNull { dayClass ->
//            dayClass.kotlin.objectInstance ?: dayClass.kotlin.createInstance()
//        }
//    }

    fun dayByClass(someClass: Class<*>): Int {
        val matchResult = Regex("""Day(\d+)\D*""").matchEntire(someClass.simpleName)
            ?: throw IllegalArgumentException("Invalid class name format.")
        return matchResult.groupValues[1].toInt()
    }

    fun findByDay(year: Int, day: Int): DaySolution {
        val className = "net.speciesm.draget.aoc$year.solutions.Day${String.format("%02d", day)}"
        return Class.forName(className).getField("INSTANCE").get(null) as DaySolution
    }
}
