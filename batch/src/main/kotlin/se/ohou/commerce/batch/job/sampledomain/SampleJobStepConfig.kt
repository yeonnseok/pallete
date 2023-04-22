package se.ohou.commerce.batch.job.sampledomain

import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import se.ohou.commerce.batch.supports.logging.Loggable

@Configuration
class SampleJobStepConfig(
    private val stepBuilderFactory: StepBuilderFactory,
) : Loggable {
    @Bean("sampleJobStep")
    @JobScope
    fun sampleJobStep(): Step {
        return stepBuilderFactory.get("sampleJobStep")
            .tasklet { _, _ ->
                // tasklet logic

                RepeatStatus.FINISHED
            }.build()
    }
}
