package kr.co.pallete.api.security

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CustomAuthenticationManager(
    private val userDetailsService: CustomUserDetailsService,
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> = Mono.fromCallable {
        val email = authentication.principal as String

        val member: UserDetails = runBlocking {
            userDetailsService.findByUsername(email).awaitFirstOrNull()
                ?: throw RuntimeException("User not found with email: $email")
        }

        return@fromCallable UsernamePasswordAuthenticationToken(
            /* aPrincipal */ member,
            /* aCredentials */ authentication.credentials,
            /* anAuthoritiesauthenticatedUser */ member.authorities,
        )
    }
}
