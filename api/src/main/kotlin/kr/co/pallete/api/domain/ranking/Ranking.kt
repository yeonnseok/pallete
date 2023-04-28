package kr.co.pallete.api.domain.ranking

// TODO: interface로 변환되지않을까
data class Ranking(
    val type: RankingType,
    val rank: Int,
    val id: String,

    val name: String, // TODO: 임시 필드
) {
    enum class RankingType {
        MALE_PROFILE,
        FEMALE_PROFILE,
    }
}
