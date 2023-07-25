package kr.co.pallete.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.pallete.api.supports.logging.Loggable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import reactor.core.publisher.Mono
import java.net.URI

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration(
    private val jwtTokenService: JwtTokenService,
) : Loggable {
    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        customServerSecurityContextRepository: CustomServerSecurityContextRepository,
        customAuthenticationManager: CustomAuthenticationManager,
    ): SecurityWebFilterChain =
        http
            .securityContextRepository(customServerSecurityContextRepository)
            .authenticationManager(customAuthenticationManager)
            .httpBasic().disable()
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource()).and()
            .formLogin().disable()
            .logout().disable() // TODO logout 설정
            .requestCache().disable()
            .authorizeExchange {
                it
                    // 인증 무시
                    .pathMatchers(
                        "/favicon.ico",
                        "/static/**",
                        "/js/**",
                        "/img/**",
                        "/css/**",
                    ).permitAll()
                    .pathMatchers("/api/v1/auth/permitted").permitAll()
                    .pathMatchers("/api/v1/auth/login").permitAll()
                    .pathMatchers("/api/v1/auth/signup").permitAll()
                    .pathMatchers("/actuator/**").permitAll()
                    .anyExchange().authenticated()
            }
            // SpringSecurityFilter 가 실행되기 전에 실행할 filter 설정
            .addFilterBefore(
                JwtAuthenticationFilter(
                    jwtTokenService,
                    customServerSecurityContextRepository,
                ),
                SecurityWebFiltersOrder.AUTHENTICATION,
            )
            // filtering 중 Not Authenticated 인 경우 exceptionHandling
            .exceptionHandling {
                it.accessDeniedHandler { exchange, exception ->
                    log.debug("### Error: ${exception.stackTraceToString()}")
                    exchange.response.statusCode = HttpStatus.FORBIDDEN

                    val dataBufferFactory = exchange.response.bufferFactory()
                    val errorDetails =
                        mapOf("error" to "Access Denied", "message" to "you don't have permission")
                    val errorJson = ObjectMapper().writeValueAsBytes(errorDetails)
                    exchange.response.writeWith(Mono.just(dataBufferFactory.wrap(errorJson)))
                }
                it.authenticationEntryPoint { exchange, exception ->
                    log.debug("### Error: ${exception.stackTraceToString()}")
                    exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                    exchange.response.headers.location = URI("/login")

                    val dataBufferFactory = exchange.response.bufferFactory()
                    val errorDetails =
                        mapOf("error" to "InvalidAuthentication", "message" to exception.message)
                    val errorJson = ObjectMapper().writeValueAsBytes(errorDetails)
                    exchange.response.writeWith(Mono.just(dataBufferFactory.wrap(errorJson)))
                }
            }
            .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
