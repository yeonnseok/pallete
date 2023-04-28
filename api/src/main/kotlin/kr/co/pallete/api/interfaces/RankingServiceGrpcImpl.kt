package kr.co.pallete.api.interfaces

import kr.co.pallete.api.domain.ranking.RankingService
import kr.co.pallete.api.interfaces.mappers.RankingProtoMapper.toProto
import kr.co.pallete.api.supports.common.orElseNull
import kr.co.pallete.api.v1.ranking.GetRankingBoardRequest
import kr.co.pallete.api.v1.ranking.GetRankingBoardResponse
import kr.co.pallete.api.v1.ranking.GetRankingRequest
import kr.co.pallete.api.v1.ranking.GetRankingResponse
import kr.co.pallete.api.v1.ranking.Ranking.RankingType
import kr.co.pallete.api.v1.ranking.RankingServiceGrpcKt
import kr.co.pallete.api.v1.ranking.getRankingBoardResponse
import kr.co.pallete.api.v1.ranking.getRankingResponse
import org.lognet.springboot.grpc.GRpcService

@GRpcService
class RankingServiceGrpcImpl(
    rankingServices: List<RankingService>,
) : RankingServiceGrpcKt.RankingServiceCoroutineImplBase() {

    private val rankingServiceMap = rankingServices.associateBy { RankingType.valueOf(it.rankingType.name) }

    override suspend fun getRankingBoard(request: GetRankingBoardRequest): GetRankingBoardResponse {
        return getRankingBoardResponse {
            rankings += determineRankingService(request.rankingType)
                .getRankingBoard(request.limit.orElseNull())
                .map { it.toProto() }
        }
    }

    override suspend fun getRanking(request: GetRankingRequest): GetRankingResponse {
        return getRankingResponse {
            ranking = determineRankingService(request.rankingType)
                .getRanking(request.id)
                .toProto()
        }
    }

    private fun determineRankingService(rankingType: RankingType): RankingService =
        rankingServiceMap[rankingType]
            ?: throw IllegalArgumentException("### 지원하지 않는 랭킹 타입 입니다.")
}
