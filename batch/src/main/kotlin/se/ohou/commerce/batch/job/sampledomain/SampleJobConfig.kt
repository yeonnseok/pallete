package se.ohou.commerce.batch.job.sampledomain

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * --job.name={job_name}
 */
@Configuration
class SampleJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
) {
    @Bean
    fun sampleJob(
        @Qualifier("sampleJobStep") sampleJobStep: Step,
    ): Job = this.jobBuilderFactory.get(SAMPLE_JOB)
        .start(sampleJobStep)
        .build()

    companion object {
        const val SAMPLE_JOB = "sampleJob"
    }
}
