package kr.co.pallete.database.document

import com.querydsl.core.annotations.QueryEntity
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 *
 * @property id ObjectId
 * @property name 이름
 * @property email 이메일
 * @property password 비밀번호
 * @property age 나이
 * @property score 점수
 * @constructor
 */

@QueryEntity
@Document(collection = "Member")
data class Member(
    @Id
    val id: ObjectId,

    val name: String,

    val email: String,

    val password: String,

    val age: Int,

    val score: Long,
)
