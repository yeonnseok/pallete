package kr.co.pallete.auth.security.common

import org.springframework.security.web.authentication.WebAuthenticationDetails
import javax.servlet.http.HttpServletRequest

class FormWebAuthenticationDetails(request: HttpServletRequest) : WebAuthenticationDetails(request) {

    val secretkey: String

    init {
        secretkey = request.getParameter("secret_key")
    }
}
