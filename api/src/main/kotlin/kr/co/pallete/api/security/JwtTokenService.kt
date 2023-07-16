package kr.co.pallete.api.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import kr.co.pallete.api.supports.logging.Loggable
import kr.co.pallete.database.document.Member
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtTokenService(
    @Value("\${spring.jwt.secret}")
    val secretKey: String,
) : Loggable {
    companion object {
        private const val TOKEN_VALID_MILLI_SECOND = 24 * 60 * 60 * 1000
        private const val EMAIL_CLAIM = "email"
        private const val ROLES_CLAIM = "roles"
        private const val NICKNAME_CLAIM = "nickname"
    }

    fun validateToken(token: String): Boolean {
        val result = runCatching {
            // validate expiration & forgery
            parseToJwsFromToken(token)
        }
        return if (result.isSuccess) {
            log.debug("### TokenValidation Success: {}", result.getOrNull())
            true
        } else {
            log.debug("### TokenValidation Error: {}", result.exceptionOrNull()?.stackTraceToString())
            false
        }
    }

    fun acquireToken(request: ServerHttpRequest): String? {
        return runCatching {
            return request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()?.substringAfter("Bearer ")
        }.getOrNull()
    }

    private fun parseToJwsFromToken(token: String): Jws<Claims> =
        try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
        } catch (e: Exception) {
            val message = "${e.message}"
            throw Exception(message, e)
        }

    private fun extractClaimsFromToken(token: String): Claims {
        val result = runCatching {
            parseToJwsFromToken(token)
        }
        return if (result.isSuccess) {
            log.debug("### TokenValidation Success: {}", result.getOrNull())
            result.getOrNull()!!.body
        } else {
            log.debug("### TokenValidation Error: {}", result.exceptionOrNull()?.stackTraceToString())
            throw result.exceptionOrNull()!!
        }
    }

    fun extractEmailFromToken(token: String): String =
        extractClaimsFromToken(token)[EMAIL_CLAIM] as String

    private fun createClaims(member: Member): Claims {
        return Jwts.claims().apply {
            subject = member.name
            this[EMAIL_CLAIM] = member.email
            this[NICKNAME_CLAIM] = member.nickname
            // this[ROLES_CLAIM] = user.roles.map { it.name } // TODO : token안에 claim으로 넣을지 고민
        }
    }

    fun createToken(member: Member): String {
        val now = Date()
        return Jwts.builder()
            .setClaims(createClaims(member))
            .setIssuedAt(now)
            .setExpiration(Date(now.time + TOKEN_VALID_MILLI_SECOND))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }
}
