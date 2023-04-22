package se.ohou.commerce.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import se.ohou.clay.armeria.config.ArmeriaConfig

@SpringBootApplication
@Import(
    value = [
        ArmeriaConfig::class,
    ]
)
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
