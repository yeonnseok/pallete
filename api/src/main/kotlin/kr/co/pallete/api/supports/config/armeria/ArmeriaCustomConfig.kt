package kr.co.pallete.api.supports.config.armeria

import com.linecorp.armeria.common.HttpMethod
import com.linecorp.armeria.common.grpc.GrpcJsonMarshaller
import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.cors.CorsService
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CorsAllowProperties::class)
class ArmeriaCustomConfig(
    val corsAllowProperties: CorsAllowProperties,
) : ArmeriaServerCustomizer {

    override fun customizeServerBuilder(builder: ServerBuilder) {
        val corsService = CorsService.builder(corsAllowProperties.origins)
            .allowCredentials()
            .allowNullOrigin()
            .allowRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
            .allowRequestHeaders("Origin", "Accept", "Content-Type", "X-Requested-With", "X-CSRF-Token")
            .preflightResponseHeader("x-preflight-cors", "Hello CORS")
            .newDecorator()
        builder.decorator(corsService)
    }

    override fun customizeGrpcServiceBuilder(builder: GrpcServiceBuilder) {
        // Reference: https://github.com/line/armeria/pull/1753/files
        // gRPC 는 default 값을 가지는 필드는 빈 필드로 내려주기 때문에, json marshalling 과정에서 default 필드를 추가하는 옵션을 추가해줍니다.
        builder
            .jsonMarshallerFactory { serviceDescriptor ->
                GrpcJsonMarshaller.builder()
                    .jsonMarshallerCustomizer { builder ->
                        builder.includingDefaultValueFields(true)
                    }
                    .build(serviceDescriptor)
            }
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "cors.allow")
data class CorsAllowProperties(
    val origins: List<String>,
)
