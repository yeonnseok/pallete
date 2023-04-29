package kr.co.pallete.auth.domain

import org.springframework.stereotype.Service

@Service
class AccountService(
    val accountRepository: AccountRepository
) {
    fun createAccount(account: Account) {
        accountRepository.save(account)
    }
}
