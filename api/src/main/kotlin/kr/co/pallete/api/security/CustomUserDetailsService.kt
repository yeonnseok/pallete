package kr.co.pallete.api.security

import kr.co.pallete.api.domain.member.MemberRepository
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository,
) : ReactiveUserDetailsService {

    override fun findByUsername(email: String?): Mono<UserDetails> {
        if (email == null) {
            return Mono.empty()
        }

        return memberRepository.findByEmail(email).map { it as UserDetails }
    }
}
