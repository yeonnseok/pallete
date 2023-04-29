package kr.co.pallete.auth.domain

class AccountRequest(
    val memberId: String,
    val password: String,
    val email: String,
    val role: String
)
