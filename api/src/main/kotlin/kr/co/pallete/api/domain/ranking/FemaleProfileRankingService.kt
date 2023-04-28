package kr.co.pallete.api.domain.ranking

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kr.co.pallete.api.domain.member.MemberRepository
import kr.co.pallete.api.domain.ranking.Ranking.RankingType
import kr.co.pallete.api.supports.cache.RankingCacheService
import kr.co.pallete.database.document.Member
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class FemaleProfileRankingService(
    private val cacheService: RankingCacheService,
    private val memberRepository: MemberRepository,
) : RankingService {
    override val rankingType: RankingType = RankingType.FEMALE_PROFILE

    override suspend fun getRankingBoard(limit: Int?): List<Ranking> {
        val scoredList = cacheService.getScoredSortedSet(
            key = FEMALE_PROFILE_RANKING_KEY,
            limit = limit ?: DEFAULT_FEMALE_PROFILE_TOP_RANK_LIMIT,
        )

        // TODO: move to batch process
        if (scoredList.isEmpty()) {
            memberRepository.findByGender(Member.Gender.FEMALE)
                .collectList()
                .awaitFirstOrNull()
                ?.let { list ->
                    val memberScoreMap = list.associateBy(
                        keySelector = { it.id.toString() },
                        valueTransform = { it.score },
                    )
                    cacheService.setScoredSortedSet(FEMALE_PROFILE_RANKING_KEY, memberScoreMap)
                }
        }
        return scoredList.mapIndexed { index, id ->
            // 임시 호출
            val member = memberRepository.findById(ObjectId(id)).awaitSingle()
            Ranking(
                type = RankingType.FEMALE_PROFILE,
                id = id,
                rank = index + 1,
                name = member.name,
            )
        }
    }

    override suspend fun getRanking(id: String): Ranking {
        val rank = cacheService.getReverseRank(FEMALE_PROFILE_RANKING_KEY, id)
        return rank?.let {
            // 임시 호출
            val member = memberRepository.findById(ObjectId(id)).awaitSingle()
            Ranking(
                type = RankingType.FEMALE_PROFILE,
                id = id,
                rank = rank + 1,
                name = member.name,
            )
        } ?: throw IllegalArgumentException("### 아직 랭킹을 계산할 수 없습니다.")
    }

    companion object {
        const val FEMALE_PROFILE_RANKING_KEY = "FEMALE_PROFILE_RANKING"
        const val DEFAULT_FEMALE_PROFILE_TOP_RANK_LIMIT = 5
    }
}
