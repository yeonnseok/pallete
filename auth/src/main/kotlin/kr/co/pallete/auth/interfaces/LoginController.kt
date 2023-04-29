package kr.co.pallete.auth.interfaces

import kr.co.pallete.auth.domain.Account
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class LoginController {

    @GetMapping("/login")
    fun login(
        @RequestParam(value = "error", required = false) error: String?,
        @RequestParam(value = "exception", required = false) exception: String?,
        model: Model
    ): String {
        model.addAttribute("error", error)
        model.addAttribute("exception", exception)

        return "user/login/login"
    }

    @GetMapping("/logout")
    fun logout(req: HttpServletRequest, resp: HttpServletResponse): String {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication != null) {
            SecurityContextLogoutHandler().logout(req, resp, authentication)
        }

        return "redirect:/login"
    }

    @GetMapping("/denied")
    fun accessDenied(
        @RequestParam(value = "exception", required = false) exception: String?,
        model: Model
    ): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val account = authentication.principal as Account

        model.addAttribute("memberId", account.memberId)
        model.addAttribute("exception", exception)

        return "user/login/denied"
    }
}
