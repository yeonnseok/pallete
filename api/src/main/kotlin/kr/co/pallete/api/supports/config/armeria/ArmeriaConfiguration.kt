package kr.co.pallete.api.supports.config.armeria

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.BindableService
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.linecorp.armeria.server.grpc.GrpcService as GRPCService

@Configuration
class ArmeriaConfiguration(
    @Value("\${grpc.docs.enabled}")
    val docsEnabled: Boolean,
) {
    @Bean
    fun autowiringGRPCService(services: List<BindableService>): GRPCService {
        return GRPCService
            .builder()
            .apply {
                services.forEach {
                    addService(it)
                }
            }
            .addService(ProtoReflectionService.newInstance())
            .supportedSerializationFormats(GrpcSerializationFormats.values())
            .enableUnframedRequests(true)
            .useBlockingTaskExecutor(true)
            .build()
    }

    @Bean
    fun armeriaServiceInitializer(gRPCService: GRPCService): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator { serverBuilder: ServerBuilder ->
            val builder = serverBuilder
                .service("prefix:/", gRPCService)
            if (docsEnabled) {
                builder.serviceUnder(
                    "/docs",
                    DocService
                        .builder()
                        .exclude(DocServiceFilter.ofServiceName(ServerReflectionGrpc.SERVICE_NAME))
                        .build(),
                )
            }
        }
    }
}
