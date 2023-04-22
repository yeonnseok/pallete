package se.ohou.commerce.consumer.supports.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration
@EnableKafka
class KafkaConfiguration {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var BOOTSTRAP_SERVER: String

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<ByteArray, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<ByteArray, String>()
        factory.setConcurrency(1)
        factory.consumerFactory = consumerFactory()
        factory.containerProperties.pollTimeout = 3000
        factory.isBatchListener = true
        return factory
    }

    @Bean
    fun consumerFactory() =
        DefaultKafkaConsumerFactory(
            getConfig(),
            ByteArrayDeserializer(),
            StringDeserializer(),
        )

    fun getConfig(): Map<String, Any> =
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to BOOTSTRAP_SERVER,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "latest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
//            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to IntegerSerializer::class.java,
//            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG to "100",
        )
}
