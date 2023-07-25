package kr.co.pallete.api.interfaces

// service RankingService {
//   rpc GetRankingBoard (GetRankingBoardRequest) returns (GetRankingBoardResponse) {
//     option (google.api.http) = {
//       get: "/api/v1/rankings"
//     };
//   }
//
//   rpc GetRanking (GetRankingRequest) returns (GetRankingResponse) {
//     option (google.api.http) = {
//       get: "/api/v1/rankings/{ranking_type}/{id}"
//     };
//   }
// }
//
// message GetRankingBoardRequest {
//   Ranking.RankingType ranking_type = 1;
//   google.protobuf.Int32Value limit = 2;
// }
//
// message GetRankingBoardResponse {
//   repeated Ranking rankings = 1;
// }
//
// message GetRankingRequest {
//   Ranking.RankingType ranking_type = 1;
//   string id = 2;
// }
//
// message GetRankingResponse {
//   Ranking ranking = 1;
// }
//
// message Ranking {
//   RankingType type = 1;
//   int32 rank = 2;
//
//   oneof item {
//     SimpleMember member = 3; // TODO : Profile로 변경
//   }
//
//   enum RankingType {
//     UNKNOWN = 0;
//     MALE_PROFILE = 1;
//     FEMALE_PROFILE = 2;
//   }
// }

// @GRpcService
// class RankingServiceGrpcImpl(
//     rankingServices: List<RankingService>,
// ) : RankingServiceGrpcKt.RankingServiceCoroutineImplBase() {
//
//     private val rankingServiceMap = rankingServices.associateBy { RankingType.valueOf(it.rankingType.name) }
//
//     override suspend fun getRankingBoard(request: GetRankingBoardRequest): GetRankingBoardResponse {
//         return getRankingBoardResponse {
//             rankings += determineRankingService(request.rankingType)
//                 .getRankingBoard(request.limit.orElseNull())
//                 .map { it.toProto() }
//         }
//     }
//
//     override suspend fun getRanking(request: GetRankingRequest): GetRankingResponse {
//         return getRankingResponse {
//             ranking = determineRankingService(request.rankingType)
//                 .getRanking(request.id)
//                 .toProto()
//         }
//     }
//
//     private fun determineRankingService(rankingType: RankingType): RankingService =
//         rankingServiceMap[rankingType]
//             ?: throw IllegalArgumentException("### 지원하지 않는 랭킹 타입 입니다.")
// }
