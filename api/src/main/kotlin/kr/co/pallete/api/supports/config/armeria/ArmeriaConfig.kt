package kr.co.pallete.api.supports.config.armeria

import com.linecorp.armeria.server.ServerListener
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.BindableService
import io.grpc.health.v1.HealthCheckResponse
import io.grpc.health.v1.HealthGrpc
import io.grpc.protobuf.services.HealthStatusManager
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Optional

@Configuration
class ArmeriaConfig {

    @Bean
    fun armeriaServerConfigurator(
        bindableServices: List<BindableService>,
        grpcStatusFunctionMappers: List<GrpcStatusFunctionMapper>,
        armeriaServerCustomizer: Optional<ArmeriaServerCustomizer>,
        @Value("\${clay.armeria.docs.enabled:true}") docsEnabled: Boolean,
    ): ArmeriaServerConfigurator =
        ArmeriaServerConfigurator { serverBuilder ->
            if (bindableServices.isEmpty()) {
                return@ArmeriaServerConfigurator
            }

            val builder = armeriaServerCustomizer.map {
                it.initializeGrpcServiceBuilder(bindableServices, grpcStatusFunctionMappers)
            }.orElseGet {
                defaultGrpcBuilder(bindableServices, grpcStatusFunctionMappers)
            }

            // https://cloud.google.com/blog/topics/developers-practitioners/health-checking-your-grpc-servers-gke
            // grpc_health_probe의 요청에 대한 응답을 Armeria 서버 상태에 맞게 처리합니다.
            val manager = HealthStatusManager().apply {
                setStatus(
                    HealthStatusManager.SERVICE_NAME_ALL_SERVICES,
                    HealthCheckResponse.ServingStatus.NOT_SERVING
                )
            }
            serverBuilder.serverListener(
                ServerListener.builder()
                    .whenStarted {
                        manager.setStatus(
                            HealthStatusManager.SERVICE_NAME_ALL_SERVICES,
                            HealthCheckResponse.ServingStatus.SERVING
                        )
                    }.whenStopping {
                        manager.setStatus(
                            HealthStatusManager.SERVICE_NAME_ALL_SERVICES,
                            HealthCheckResponse.ServingStatus.NOT_SERVING
                        )
                    }.build()
            )
            builder.addService(manager.healthService)

            armeriaServerCustomizer.map { it.customizeGrpcServiceBuilder(builder) }

            serverBuilder.service(
                builder.build(),
            )

            if (docsEnabled) {
                serverBuilder.serviceUnder(
                    "/docs",
                    DocService.builder()
                        .exclude(
                            DocServiceFilter.ofServiceName(ServerReflectionGrpc.SERVICE_NAME)
                                .or(DocServiceFilter.ofServiceName(HealthGrpc.SERVICE_NAME))
                        )
                        .build()
                )
            }

            armeriaServerCustomizer.map { it.customizeServerBuilder(serverBuilder) }
        }

    companion object {
        fun defaultGrpcBuilder(
            bindableServices: List<BindableService>,
            grpcStatusFunctionMappers: List<GrpcStatusFunctionMapper>
        ): GrpcServiceBuilder =
            GrpcService.builder()
                // DocService 에서 json 으로 gRPC 요청을 보낼수 있게 합니다.
                .enableUnframedRequests(true)
                // HTTP/JSON to gRPC transcoding 기능을 사용합니다.
                // HTTP endpoint 는 proto 파일의 gRPC service method 정의 부분에서 등록 가능합니다.
                // https://cloud.google.com/endpoints/docs/grpc/transcoding
                .enableHttpJsonTranscoding(true)
                // 대부분의 API 처리 시 DB 접근이 이루어지기 때문에 별도의 executor 를 이용해서 API 를 처리하게 합니다.
                // 추후에 이 부분은 optional 로 변경해도 됩니다.
                .useBlockingTaskExecutor(true)
                .addService(ProtoReflectionService.newInstance())
                .apply {
                    if (grpcStatusFunctionMappers.isEmpty()) {
                        // 기본 exception translator 를 설정합니다.
                    } else {
                        grpcStatusFunctionMappers.forEach {
                            addExceptionMapping(it.clazz, it.function)
                        }
                    }
                    bindableServices.forEach(this::addService)
                }
    }
}
