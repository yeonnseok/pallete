package kr.co.pallete.api.domain.member

import kr.co.pallete.database.document.Member
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor

interface MemberRepository :
    ReactiveMongoRepository<Member, ObjectId>,
    ReactiveQuerydslPredicateExecutor<Member>
