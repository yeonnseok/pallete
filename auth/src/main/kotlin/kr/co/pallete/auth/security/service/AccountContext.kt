package kr.co.pallete.auth.security.service

import kr.co.pallete.auth.domain.Account
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AccountContext(
    private val account: Account,
    authorities: Collection<GrantedAuthority>
) : User(account.memberId, account.password, authorities) {

    fun getAccount(): Account {
        return account
    }
}
