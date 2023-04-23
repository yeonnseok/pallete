package kr.co.pallete.batch

import kr.co.pallete.batch.supports.config.BASE_CLASS_PATH
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@EnableBatchProcessing
@SpringBootApplication(
    scanBasePackages = [BASE_CLASS_PATH],
)
class BatchApplication

fun main(args: Array<String>) {
    val context = runApplication<BatchApplication>(*args)
    exitProcess(SpringApplication.exit(context))
}
