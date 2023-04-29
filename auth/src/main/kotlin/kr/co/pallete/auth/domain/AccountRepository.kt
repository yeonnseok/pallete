package kr.co.pallete.auth.domain

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {

    fun findByMemberId(memberId: String): Account?
}
