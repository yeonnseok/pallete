package kr.co.pallete.api.supports.config.armeria

import com.linecorp.armeria.common.grpc.GrpcJsonMarshaller
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder
import org.springframework.context.annotation.Configuration

@Configuration
class ArmeriaCustomConfig() : ArmeriaServerCustomizer {

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
