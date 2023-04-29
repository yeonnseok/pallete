package kr.co.pallete.auth.security.service

import kr.co.pallete.auth.domain.Account
import kr.co.pallete.auth.domain.AccountRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    val accountRepository: AccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(memberId: String): UserDetails {
        val account: Account = accountRepository.findByMemberId(memberId)
            ?: throw UsernameNotFoundException("User Not Found Exception!")

        val roles = listOf(
            SimpleGrantedAuthority(account.role.name)
        )
        return AccountContext(account, roles)
    }
}
