package net.speciesm.draget.aoc2024.utils.aoc

import net.speciesm.draget.aoc2024.utils.XDGUtils
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.NoSuchDriverException
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.seconds

object AoCSession {
    fun configPath(year: Int) = "${XDGUtils.getConfigHome("draget_aoc")}/$year/draget_aoc$year.properties"

    fun getSession(year: Int, interactiveLogin: Boolean): String {
        val envSession = System.getenv("AOC_SESSION")
        if (envSession != null) {
            println("Using 'AOC_SESSION' for session.")
            return envSession
        } else {
            val aocSavedSession = readSession(configPath(year))
            return if (aocSavedSession != null) {
                println("Loaded session from properties.")
                aocSavedSession
            } else if (interactiveLogin) {
                val aocSession = getSessionInteractivelyOrQuit()
                println("Saving new session…")
                storeSession(configPath(year), aocSession)
                aocSession
            } else {
                System.err.println("Failed load saved AoC session. Set the AOC_SESSION variable or use --interactive-login to login and store the session.")
                exitProcess(1)
            }
        }
    }

    private fun readSession(filePath: String): String? {
        try {
            val properties = Properties()
            FileReader(filePath).use {
                properties.load(it)
                return properties.getProperty("session")
            }
        } catch (_: Exception) {
            return null
        }
    }

    private fun storeSession(filePath: String, aocSession: String) {
        val properties = Properties()
        properties.setProperty("session", aocSession)
        FileWriter(filePath).use {
            properties.store(it, "# This is your AoC session, do not share or commit this publicly")
        }
    }

    enum class AuthState { BeforeAuth, ActiveAuth }

    private fun getSessionInteractivelyOrQuit(): String {
        val aocURLs = listOf("https://adventofcode.com")
        val authURLs = listOf("https://github.com")
        val sessionTimeoutMillis = 120.seconds.inWholeMilliseconds
        val sleepMillis = 1000L

        val driver = initWebDriverOrQuit()
        var state = AuthState.BeforeAuth

        println("Trying to authenticate interactively…")
        repeat((sessionTimeoutMillis / sleepMillis).toInt()) {
            try {
                val currentURL = driver.currentUrl

                if ((authURLs + aocURLs).none { currentURL.contains(it) }) {
                    System.err.println("Failed to follow auth flow, quitting.")
                    exitProcess(1)
                }

                state = if (state == AuthState.BeforeAuth && authURLs.any { currentURL.contains(it) }) {
                    println("Detected active auth flow…")
                    AuthState.ActiveAuth
                } else {
                    state
                }

                if (state == AuthState.ActiveAuth && aocURLs.any { currentURL.contains(it) }) {
                    println("\nReturned back to AoC, reading session…")
                    val session = driver.manage().cookies.firstOrNull { it.name == "session" }?.value
                    driver.quit()
                    if (session != null) return session

                    System.err.println("Failed to read session token, quitting.")
                    exitProcess(1)
                }
            } catch (e: WebDriverException) {
                System.err.println("There was an auth error: ${e.message}")
                exitProcess(1)
            }
            Thread.sleep(sleepMillis)
            print(".")
        }
        System.err.println("Timeout on auth, quitting.")
        exitProcess(1)
    }

    private fun initWebDriverOrQuit(): WebDriver {
        val maxAttempts = 3;
        val sleepMillis = 1000L
        val authURL = "https://adventofcode.com/auth/github"

        println("Initializing Firefox web driver…")
        for (attempt in maxAttempts downTo 1) {
            try {
                val driver = FirefoxDriver()
                Thread.sleep(sleepMillis)
                driver.get(authURL)
                Runtime.getRuntime().addShutdownHook(Thread { driver.quit() })
                return driver
            } catch (e: NoSuchDriverException) {
                val retryMessage = if (attempt > 1) "\nTrying again…" else ""
                System.err.println("Failed to open browser: ${e.message ?: "Unknown error"}$retryMessage")
            }
        }
        System.err.println("Failed to open browser after $maxAttempts, quitting.")
        exitProcess(1)
    }
}
