package kr.co.pallete.api.supports.cache

import com.google.protobuf.Message
import com.google.protobuf.MessageLite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import kr.co.pallete.api.supports.logging.Loggable
import kr.co.pallete.api.v1.member.GetMemberRequest
import kr.co.pallete.api.v1.member.GetMemberResponse
import org.redisson.api.RedissonReactiveClient
import org.redisson.client.codec.ByteArrayCodec
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit

abstract class CacheServiceImpl<T : Message, K>(
    private val redissonClient: RedissonReactiveClient,
    private val iODispatcher: CoroutineDispatcher,
) : Loggable, CacheService<T, K> {
    abstract val keySelector: (K) -> String

    override suspend fun <T : MessageLite> getProtoBufData(keySource: K, type: Class<T>): T? =
        try {
            withContext(iODispatcher) {
                redissonClient.getBucket<ByteArray>(keySelector.invoke(keySource), ByteArrayCodec.INSTANCE)
                    .get()
                    .awaitFirstOrNull()?.let {
                    val builder = type.getDeclaredMethod("newBuilder").invoke(null) as MessageLite.Builder
                    builder.mergeFrom(it)
                    builder.build() as T
                }
            }
        } catch (e: Exception) {
            log.error("### [Pallete CACHE] cache get for ${keySelector.invoke(keySource)} failed: ${e.message}", e)
            null
        }

    override suspend fun <T : MessageLite> setProtoBufData(keySource: K, item: T, expire: Duration) {
        try {
            withContext(iODispatcher) {
                redissonClient
                    .getBucket<ByteArray>(keySelector.invoke(keySource), ByteArrayCodec.INSTANCE)
                    .set(item.toByteArray(), expire.seconds, TimeUnit.SECONDS)
                    .awaitSingle()
            }
        } catch (e: Exception) {
            log.error("### [Pallete CACHE] cache get for ${keySelector.invoke(keySource)} failed: ${e.message}", e)
        }
    }

    override suspend fun clearAll() {
        try {
            withContext(iODispatcher) {
                redissonClient
                    .keys
                    .flushdb()
                    .awaitSingle()
            }
        } catch (e: Exception) {
            log.error("### [Pallete CACHE] cache clear failed: ${e.message}", e)
        }
    }
}

@Component
class MemberCacheService(
    redissonClient: RedissonReactiveClient,
    iODispatcher: CoroutineDispatcher,
) : CacheServiceImpl<GetMemberResponse, GetMemberRequest>(redissonClient, iODispatcher) {
    override val keySelector: (GetMemberRequest) -> String = { "member:${it.id}" }
}
