package kr.co.pallete.api.supports.cache

import kotlinx.coroutines.CoroutineDispatcher
import kr.co.pallete.api.v1.member.GetMemberRequest
import kr.co.pallete.api.v1.member.GetMemberResponse
import org.redisson.api.RedissonReactiveClient
import org.springframework.stereotype.Component

@Component
class MemberCacheService(
    redissonClient: RedissonReactiveClient,
    iODispatcher: CoroutineDispatcher,
) : ProtoCacheServiceImpl<GetMemberResponse, GetMemberRequest>(redissonClient, iODispatcher) {
    override val keySelector: (GetMemberRequest) -> String = { "member:${it.id}" }
}
