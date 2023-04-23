package kr.co.pallete.api.supports.config.armeria

import com.linecorp.armeria.common.grpc.GrpcStatusFunction

data class GrpcStatusFunctionMapper(
    val clazz: Class<out Throwable> = Throwable::class.java,
    val function: GrpcStatusFunction,
)
