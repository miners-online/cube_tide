package uk.minersonline.cube_tide

import java.io.File


object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val javaPath = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"

        // JVM arguments for opening required modules
        val jvmArgs = listOf(
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED"
        )

        // Arguments for launching your actual Kotlin program (AppMain)
        val appMainArgs = listOf(
            "-cp", System.getProperty("java.class.path"), // Classpath for Kotlin program and dependencies
            "uk.minersonline.cube_tide.AppMain" // The actual main class
        ) + args

        // Combine JVM and AppMain arguments
        val processArgs = listOf(javaPath) + jvmArgs + appMainArgs

        // Start the process
        val processBuilder = ProcessBuilder(processArgs)
        processBuilder.inheritIO() // Output should be visible in the terminal
        val process = processBuilder.start()

        // Wait for your Kotlin program (AppMain) to finish
        val exitCode = process.waitFor()
        println("AppMain exited with code: $exitCode")
    }
}