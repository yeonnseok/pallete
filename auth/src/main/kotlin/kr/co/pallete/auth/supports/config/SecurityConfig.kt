package kr.co.pallete.auth.supports.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration(
    val userDetailsService: ReactiveUserDetailsService,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .authorizeExchange()
            .pathMatchers("/admin/**").hasRole("ADMIN")
            .pathMatchers("/api/**").hasAnyRole("USER", "ADMIN")
            .anyExchange().authenticated()
            .and().formLogin()
            .and().httpBasic()
            .and().csrf().disable()
            .build()
    }

    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        val authManager = UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
        authManager.setPasswordEncoder(passwordEncoder())
        return authManager
    }

    @Bean
    fun securityContextRepository(): ServerSecurityContextRepository {
        return WebSessionServerSecurityContextRepository()
            .apply {
                setSpringSecurityContextAttrName(
                    WebSessionServerSecurityContextRepository.DEFAULT_SPRING_SECURITY_CONTEXT_ATTR_NAME,
                )
            }
    }
}
