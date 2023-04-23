package kr.co.pallete.api.domain

import kr.co.pallete.database.document.Member
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class MemberService {

    fun getMember(id: String): Member {
        return Member(
            id = ObjectId(),
            name = "",
            email = "",
            password = "",
            age = 0,
        )
    }
}
