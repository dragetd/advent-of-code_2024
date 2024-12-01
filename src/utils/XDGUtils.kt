package net.speciesm.draget.aoc2024.utils

import java.io.File

object XDGUtils {
    private val operatingSystem = System.getProperty("os.name").lowercase()

    fun getConfigHome(appName: String): String {
        val configDir: File = if (isUnix()) {
            val xdgConfigHome = System.getenv("XDG_CONFIG_HOME")
            if (xdgConfigHome != null) {
                File("$xdgConfigHome/$appName")
            } else {
                File(System.getProperty("user.home"), ".config/$appName")
            }
        } else {
            File(System.getProperty("user.home"), ".$appName")
        }

        if (!configDir.exists()) {
            configDir.mkdirs()
        }
        return configDir.path
    }

    private fun isUnix(): Boolean {
        return operatingSystem.contains("nix") || operatingSystem.contains("nux") || operatingSystem.contains("mac")
    }
}
