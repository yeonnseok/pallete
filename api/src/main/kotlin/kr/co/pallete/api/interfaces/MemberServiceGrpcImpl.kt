package kr.co.pallete.api.interfaces

// @GRpcService
// class MemberServiceGrpcImpl(
//     val memberService: MemberService,
//     val cacheService: MemberCacheService,
// ) : MemberServiceGrpcKt.MemberServiceCoroutineImplBase() {
//
//     override suspend fun getMember(request: GetMemberRequest): GetMemberResponse {
//         return cacheService.getProtoBufData(request, GetMemberResponse::class.java) ?: let {
//             val id = request.id
//             val member = memberService.getMember(id)
//
//             getMemberResponse {
//                 this.member = member.toProto()
//             }.also {
//                 cacheService.setProtoBufData(request, it, Duration.ofSeconds(MEMBER_PROTO_CACHE_EXPIRE_TIME))
//             }
//         }
//     }
//
//     companion object {
//         const val MEMBER_PROTO_CACHE_EXPIRE_TIME = 60L
//     }
// }
