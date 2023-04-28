package kr.co.pallete.api.supports.cache

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import kr.co.pallete.api.supports.logging.Loggable
import org.redisson.api.RedissonReactiveClient
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class RankingCacheServiceImpl(
    val redissonClient: RedissonReactiveClient,
    private val iODispatcher: CoroutineDispatcher,
) : Loggable, RankingCacheService {

    override suspend fun setScoredSortedSet(key: String, map: Map<String, Float>) {
        try {
            withContext(iODispatcher) {
                val sortedSet = redissonClient.getScoredSortedSet<String>(key)
                Flux.fromIterable(map.entries)
                    .flatMap { sortedSet.addScore(it.key, it.value) }
                    .awaitSingle()
            }
        } catch (e: Exception) {
            log.error("### [Pallete CACHE] sortedSet set for $key failed: ${e.message}", e)
        }
    }

    override suspend fun getScoredSortedSet(key: String, limit: Int): List<String> {
        return try {
            withContext(iODispatcher) {
                val sortedSet = redissonClient.getScoredSortedSet<String>(key)
                sortedSet.valueRangeReversed(0, limit - 1).awaitSingle().toList()
            }
        } catch (e: Exception) {
            log.error("### [Pallete CACHE] sortedSet get for $key failed: ${e.message}", e)
            emptyList()
        }
    }

    override suspend fun getReverseRank(key: String, id: String): Int? {
        return try {
            withContext(iODispatcher) {
                val sortedSet = redissonClient.getScoredSortedSet<String>(key)
                sortedSet.revRank(id).awaitFirstOrNull() ?: let { null }
            }
        } catch (e: Exception) {
            log.error("### [Pallete CACHE] getRank for $key failed: ${e.message}", e)
            -1
        }
    }
}
