package kr.co.pallete.batch.supports.config

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

const val BASE_CLASS_PATH = "se.ohou.commerce.batch"

@Configuration
class BatchConfiguration : DefaultBatchConfigurer() {
    override fun setDataSource(dataSource: DataSource) {
        // override to do not set datasource even if a datasource exist.
        // initialize will use a Map based JobRepository (instead of database)
        // Spring Batch 의 메타 테이블들을 사용하지 않기 위한 설정.
    }
}
