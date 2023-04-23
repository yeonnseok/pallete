package kr.co.pallete.api.supports.config.armeria

import com.linecorp.armeria.server.ServerBuilder
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder
import io.grpc.BindableService

interface ArmeriaServerCustomizer {

    /**
     * gRPC 서비스를 완전히 새롭게 구성하고 싶은 경우에는 이 method를 구현합니다.
     * 이 method를 통해 [GrpcServiceBuilder]를 별도로 생성하더라도 health check를 위한 서비스는 자동으로 등록 됩니다.
     */
    fun initializeGrpcServiceBuilder(
        bindableServices: List<BindableService>,
        grpcStatusFunctionMappers: List<GrpcStatusFunctionMapper>,
    ): GrpcServiceBuilder =
        ArmeriaConfig.defaultGrpcBuilder(bindableServices, grpcStatusFunctionMappers)

    /**
     * gRPC 서비스 구성 이후에 추가 설정을 하고 싶은 경우에는 이 method를 구현합니다.
     */
    fun customizeGrpcServiceBuilder(builder: GrpcServiceBuilder) {}

    /**
     * Armeria 서버 구성 이후에 추가 설정을 하고 싶은 경우에는 이 method를 구현합니다.
     */
    fun customizeServerBuilder(builder: ServerBuilder) {}
}
