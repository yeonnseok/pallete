package kr.co.pallete.api.security

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * [SecurityContext]에 인증정보를 저장하는 repository 구현.
 * [JwtAuthenticationFilter]에서 토큰 인증에 성공하면 save 메서드를 호출해서 context를 저장한다.
 */
@Component
class CustomServerSecurityContextRepository(
    private val authenticationManager: ReactiveAuthenticationManager,
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        val authentication = runBlocking {
            authenticationManager.authenticate(context.authentication).awaitFirstOrNull()
        }
        SecurityContextHolder.getContext().authentication = authentication
        return Mono.empty()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> =
        // TODO exchange에 해당하는 context만 가져오는 지 확인 필요
        Mono.justOrEmpty(SecurityContextHolder.getContext())
}
