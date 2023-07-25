package kr.co.pallete.api.interfaces

import kr.co.pallete.api.domain.member.MemberService
import kr.co.pallete.api.interfaces.dto.LoginDto
import kr.co.pallete.api.interfaces.dto.SignupDto
import kr.co.pallete.api.security.JwtTokenService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val memberService: MemberService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenService: JwtTokenService,
) {

    @GetMapping("/permitted")
    fun permitted(): String {
        return "permitted"
    }

    @PostMapping("/signup")
    suspend fun signup(
        @RequestBody signupRequest: SignupDto.Request,
    ): ResponseEntity<SignupDto.Response> {
        val member = memberService.createMember(signupRequest.toMember(passwordEncoder))
        val token = jwtTokenService.createToken(member)

        return ResponseEntity.ok(SignupDto.Response(token))
    }

    @PostMapping("/login")
    suspend fun login(
        @RequestBody loginRequest: LoginDto.Request,
    ): ResponseEntity<LoginDto.Response> {
        val member = memberService.findMemberByEmail(loginRequest.email)
        check(!passwordEncoder.matches(loginRequest.password, member.password)) {
            throw IllegalArgumentException("### password not matched")
        }

        val token = jwtTokenService.createToken(member)
        return ResponseEntity.ok(LoginDto.Response(token))
    }
}
