package org.jub.kotlin.hometask4

import java.util.concurrent.Callable
import kotlin.random.Random

// Клас Task, який реалізує Callable для асинхронного виконання
class Task(private val taskId: Int, private val taskTime: Long) : Callable<String> {
    override fun call(): String {
        Thread.sleep(taskTime)
        return "Task $taskId completed in $taskTime ms"
    }
}
package org.jub.kotlin.hometask4

import java.io.File
import java.util.concurrent.*

class TaskExecutor(private val resultsFile: File) {
    private val executorService: ExecutorService = Executors.newFixedThreadPool(6)
    private val taskQueue: LinkedBlockingQueue<Callable<String>> = LinkedBlockingQueue()
    private val results = mutableListOf<String>()
    private var isFinished = false

    fun executeTask(taskName: String, taskId: Int) {
        if (isFinished) {
            println("Cannot add new tasks, the program has finished.")
            return
        }
        val task = Task(taskId, Random.nextLong(300, 5000))
        taskQueue.add(task)
    
        executorService.submit {
            val result = task.call()
            synchronized(results) {
                results.add("$taskName: $result")
            }
            writeToFile()
        }
    }

    fun getLastResult(): String {
        synchronized(results) {
            return if (results.isNotEmpty()) results.last() else "No results yet"
        }
    }

    fun finish(graceful: Boolean) {
        isFinished = true
        if (graceful) {
            executorService.shutdown()
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow()
            }
        } else {
            executorService.shutdownNow()
        }
    }

    fun cleanResultsFile() {
        resultsFile.writeText("")
    }

    private fun writeToFile() {
        synchronized(results) {
            try {
                resultsFile.printWriter().use { out ->
                    results.forEach {
                        out.println(it)
                    }
                }
            } catch (e: Exception) {
                println("Error writing to file: ${e.message}")
            }
        }
    }

    fun showHelp() {
        println("""Commands:
            task NAME X: Execute task X, name it NAME, and write the result to the results file
            get: Output the last result and its name to the console
            finish grace: Stop accepting new tasks, finish all pending tasks, and stop the application
            finish force: Stop the application without waiting for pending tasks to finish
            clean: Clean the results file
            help: Output guidelines to the console""")
    }
}