package org.jub.kotlin.hometask4

import java.io.File
import java.util.Scanner

fun main() {
    val resultsFile = File("results.txt")
    val taskExecutor = TaskExecutor(resultsFile)
    val scanner = Scanner(System.`in`)

    while (scanner.hasNextLine()) {
        val command = scanner.nextLine().trim()
        if (command == "finish grace" || command == "finish force") {
            taskExecutor.finish(graceful = command == "finish grace")
            break
        }

        when {
            command.startsWith("task") -> {
                val parts = command.split(" ")
                if (parts.size != 3) {
                    println("Invalid task command. Expected format: task NAME X")
                    continue
                }
                val taskName = parts[1]
                val taskId = try {
                    parts[2].toInt()
                } catch (e: NumberFormatException) {
                    println("Invalid task ID. It should be an integer.")
                    continue
                }
                if (taskName.contains(" ")) {
                    println("Task name cannot contain spaces.")
                    continue
                }
                taskExecutor.executeTask(taskName, taskId)
            }
            command == "get" -> {
                println(taskExecutor.getLastResult())
            }
            command == "finish grace" -> {
                taskExecutor.finish(graceful = true)
                break
            }
            command == "finish force" -> {
                taskExecutor.finish(graceful = false)
                break
            }
            command == "clean" -> {
                taskExecutor.cleanResultsFile()
            }
            command == "help" -> {
                taskExecutor.showHelp()
            }
            else -> {
                println("Unknown command. Type 'help' for a list of commands.")
            }
        }
    }
}