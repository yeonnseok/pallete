package kr.co.pallete.auth.security.provider

import kr.co.pallete.auth.security.common.FormWebAuthenticationDetails
import kr.co.pallete.auth.security.service.AccountContext
import kr.co.pallete.auth.security.service.CustomUserDetailsService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationProvider(
    private val userDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials as String

        val accountContext = userDetailsService.loadUserByUsername(username) as AccountContext

        if (!passwordEncoder.matches(password, accountContext.getAccount().password)) {
            throw BadCredentialsException("BadCredentialsException")
        }

        // 시크릿 키 인증
        val formWebAuthenticationDetails = authentication.details as FormWebAuthenticationDetails
        val secretkey = formWebAuthenticationDetails.secretkey
        if (secretkey != "secret") {
            throw InsufficientAuthenticationException("InsufficientAuthenticationException")
        }

        return UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}
