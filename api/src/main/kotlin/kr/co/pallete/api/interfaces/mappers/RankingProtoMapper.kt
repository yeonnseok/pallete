package kr.co.pallete.api.interfaces.mappers

import kr.co.pallete.api.domain.ranking.Ranking
import kr.co.pallete.api.v1.member.simpleMember
import kr.co.pallete.api.v1.ranking.Ranking.RankingType
import kr.co.pallete.api.v1.ranking.ranking

object RankingProtoMapper {

    fun Ranking.toProto() = ranking {
        type = RankingType.valueOf(this@toProto.type.name)
        rank = this@toProto.rank
        when (type) {
            in setOf(RankingType.MALE_PROFILE, RankingType.FEMALE_PROFILE) -> {
                member = simpleMember {
                    id = this@toProto.id
                    name = this@toProto.name
                }
            }
            else -> throw IllegalArgumentException("### 지원하지 않는 랭킹 타입입니다.")
        }
    }
}
