package kr.co.pallete.database.document

import com.querydsl.core.annotations.QueryEntity
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 *
 * @property id ObjectId
 * @property nickname 닉네임
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
    val id: ObjectId? = null,

    val nickname: String,

    val name: String,

    val email: String,

    val encryptedPassword: String,

    val gender: Gender,

    val age: Int,

    val score: Float,

    val roles: List<String> = listOf("USER"),

) : UserDetails {
    enum class Gender {
        MALE,
        FEMALE,
        OTHER,
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { GrantedAuthority { "ROLE_$it" } }.toMutableList()
    }

    override fun getPassword(): String {
        return encryptedPassword
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
