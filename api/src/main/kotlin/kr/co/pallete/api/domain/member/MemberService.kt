package kr.co.pallete.api.domain.member

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kr.co.pallete.database.document.Member
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class MemberService(
    val memberRepository: MemberRepository,
) {
    suspend fun getMember(id: String): Member {
        return memberRepository.findById(ObjectId(id))
            .awaitFirstOrNull() ?: throw IllegalStateException("### not found member of id : $id")
    }

    suspend fun findMemberByEmail(email: String): Member {
        return memberRepository.findByEmail(email)
            .awaitFirstOrNull() ?: throw IllegalStateException("### not found member of email : $email")
    }

    suspend fun createMember(member: Member): Member {
        return memberRepository.save(member).awaitFirst()
    }
}
