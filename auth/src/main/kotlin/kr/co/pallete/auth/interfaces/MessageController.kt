package kr.co.pallete.auth.interfaces

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MessageController {

    @GetMapping("/messages")
    fun mesage(): String {
        return "user/messages"
    }
}
