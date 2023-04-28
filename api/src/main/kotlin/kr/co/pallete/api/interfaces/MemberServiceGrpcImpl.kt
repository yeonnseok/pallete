package kr.co.pallete.api.interfaces

import kr.co.pallete.api.domain.member.MemberService
import kr.co.pallete.api.interfaces.MemberProtoMapper.toProto
import kr.co.pallete.api.supports.cache.MemberCacheService
import kr.co.pallete.api.v1.member.GetMemberRequest
import kr.co.pallete.api.v1.member.GetMemberResponse
import kr.co.pallete.api.v1.member.MemberServiceGrpcKt
import kr.co.pallete.api.v1.member.getMemberResponse
import org.lognet.springboot.grpc.GRpcService
import java.time.Duration

@GRpcService
class MemberServiceGrpcImpl(
    val memberService: MemberService,
    val cacheService: MemberCacheService,
) : MemberServiceGrpcKt.MemberServiceCoroutineImplBase() {
    override suspend fun getMember(request: GetMemberRequest): GetMemberResponse {
        return cacheService.getProtoBufData(request, GetMemberResponse::class.java) ?: let {
            val id = request.id
            val member = memberService.getMember(id)

            getMemberResponse {
                this.member = member.toProto()
            }.also {
                cacheService.setProtoBufData(request, it, Duration.ofSeconds(20))
            }
        }
    }
}
