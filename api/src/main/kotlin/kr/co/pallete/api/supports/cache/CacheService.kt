package kr.co.pallete.api.supports.cache

import com.google.protobuf.MessageLite
import java.time.Duration

interface CacheService<T, K> {

    suspend fun <T : MessageLite> getProtoBufData(keySource: K, type: Class<T>): T?

    suspend fun <T : MessageLite> setProtoBufData(keySource: K, item: T, expire: Duration)

    suspend fun clearAll()
}
