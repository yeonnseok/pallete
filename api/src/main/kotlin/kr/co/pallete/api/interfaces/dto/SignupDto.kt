package kr.co.pallete.api.interfaces.dto

import kr.co.pallete.database.document.Member
import org.springframework.security.crypto.password.PasswordEncoder

object SignupDto {

    data class Request(
        val email: String,
        val password: String,
        val nickname: String,
        val gender: String,
        val name: String,
        val age: Int,
    ) {
        fun toMember(passwordEncoder: PasswordEncoder): Member {
            return Member(
                nickname = nickname,
                name = name,
                email = email,
                gender = Member.Gender.valueOf(gender),
                encryptedPassword = passwordEncoder.encode(password),
                age = age,
                score = 0f,
            )
        }
    }

    data class Response(
        val token: String,
    )
}
