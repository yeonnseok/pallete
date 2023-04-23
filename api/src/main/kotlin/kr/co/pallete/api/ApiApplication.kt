package kr.co.pallete.api

import kr.co.pallete.api.supports.config.armeria.ArmeriaConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@SpringBootApplication
@ComponentScan(basePackages = ["kr.co.pallete"])
@Import(
    value = [
        ArmeriaConfig::class,
    ]
)
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
