package kr.co.pallete.api.domain.member

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
            .awaitFirstOrNull() ?: throw IllegalStateException("### not found member : $id")
    }
}
