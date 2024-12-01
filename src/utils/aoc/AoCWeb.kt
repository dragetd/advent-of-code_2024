package net.speciesm.draget.aoc2024.utils.aoc

import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

object AoCWeb {
    fun downloadInput(year: Int, day: Int, session: String): String {
        return downloadURLContent("https://adventofcode.com/$year/day/$day/input", "session=$session")
    }

    private fun downloadURLContent(urlStr: String, cookie: String): String {
        val url = URI(urlStr).toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("Cookie", cookie)

        return connection.inputStream.bufferedReader().use {
            it.readText().trimEnd()
        }
    }
}
