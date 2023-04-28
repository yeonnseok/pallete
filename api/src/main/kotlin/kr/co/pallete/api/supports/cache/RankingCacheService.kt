package kr.co.pallete.api.supports.cache

interface RankingCacheService {

    suspend fun setScoredSortedSet(key: String, map: Map<String, Float>)

    suspend fun getScoredSortedSet(key: String, limit: Int): List<String>

    suspend fun getReverseRank(key: String, id: String): Int?
}
