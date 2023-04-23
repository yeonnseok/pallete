package kr.co.pallete.api.interfaces

import kr.co.pallete.api.domain.MemberService
import kr.co.pallete.api.interfaces.MemberProtoMapper.toProto
import kr.co.pallete.api.v1.member.GetMemberRequest
import kr.co.pallete.api.v1.member.GetMemberResponse
import kr.co.pallete.api.v1.member.MemberServiceGrpcKt
import kr.co.pallete.api.v1.member.getMemberResponse
import org.lognet.springboot.grpc.GRpcService

@GRpcService
class MemberServiceGrpcImpl(
    val memberService: MemberService,
) : MemberServiceGrpcKt.MemberServiceCoroutineImplBase() {
    override suspend fun getMember(request: GetMemberRequest): GetMemberResponse {
        val id = request.id
        val member = memberService.getMember(id)

        return getMemberResponse {
            this.member = member.toProto()
        }
    }
}
