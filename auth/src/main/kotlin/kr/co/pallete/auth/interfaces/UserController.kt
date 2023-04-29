package kr.co.pallete.auth.interfaces

import kr.co.pallete.auth.domain.Account
import kr.co.pallete.auth.domain.AccountRequest
import kr.co.pallete.auth.domain.AccountService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class UserController(
    val accountService: AccountService,
    val passwordEncoder: PasswordEncoder
) {

    @GetMapping("/mypage")
    fun myPage(): String {
        return "user/mypage"
    }

    @GetMapping("/users")
    fun createUser(): String? {
        return "user/login/register"
    }

    @PostMapping("/users")
    fun createUser(request: AccountRequest): String? {
        val account = with(request) {
            Account(
                memberId = this.memberId,
                email = this.email,
                password = passwordEncoder.encode(this.password),
                role = Account.Role.valueOf(this.role)
            )
        }
        accountService.createAccount(account)
        return "redirect:/"
    }
}
