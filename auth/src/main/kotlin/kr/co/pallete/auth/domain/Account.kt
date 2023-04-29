package kr.co.pallete.auth.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val memberId: String,
    val password: String,
    val email: String,
    val role: Role
) {
    enum class Role {
        ADMIN,
        MANAGER,
        USER,
    }
}
