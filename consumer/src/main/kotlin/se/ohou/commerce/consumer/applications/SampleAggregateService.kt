package se.ohou.commerce.consumer.applications

import org.springframework.stereotype.Service

/**
 * 두 도메인 서비스를 호출하여 데이터를 aggregation하는 용도로 작성하는 서비스입니다.
 * (특정 도메인 내부에서는 다른 도메인에 의존성을 가질 수 없기 때문)
 */
@Service
class SampleAggregateService
