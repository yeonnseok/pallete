package kr.co.pallete.database.document

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Member")
data class Member(
    @Id
    val id: ObjectId,

    val name: String,

    val email: String,

    val password: String,

    val age: Int,
)
