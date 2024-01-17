package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
    println("Server running at http://localhost:8080/")
    println("Ensure you have set the TEMPORAL_CLOUD_API_KEY environment variable.")
}
