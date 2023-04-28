package kr.co.pallete.api.domain.member

import kr.co.pallete.database.document.Member
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor
import reactor.core.publisher.Flux

interface MemberRepository :
    ReactiveMongoRepository<Member, ObjectId>,
    ReactiveQuerydslPredicateExecutor<Member> {
    fun findByGender(male: Member.Gender): Flux<Member>
}
