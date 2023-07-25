package kr.co.pallete.api.security

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.util.Strings
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService,
    private val serverSecurityContextRepository: CustomServerSecurityContextRepository,
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token: String? = jwtTokenService.acquireToken(exchange.request)
        if (token.isNullOrBlank()) {
            return chain.filter(exchange)
        }
        if (jwtTokenService.validateToken(token)) {
            val email: String = jwtTokenService.extractEmailFromToken(token)
            val authentication = UsernamePasswordAuthenticationToken(email, Strings.EMPTY, emptyList())
            runBlocking {
                serverSecurityContextRepository.save(exchange, SecurityContextImpl(authentication))
                    .awaitFirstOrNull()
            }
        } else {
            exchange.response.statusCode = HttpStatus.UNAUTHORIZED
            return exchange.response.setComplete()
        }
        return chain.filter(exchange)
    }
}
