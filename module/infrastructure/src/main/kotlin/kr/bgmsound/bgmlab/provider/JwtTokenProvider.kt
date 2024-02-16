package kr.bgmsound.bgmlab.provider

import io.jsonwebtoken.Jwts
import kr.bgmsound.bgmlab.model.Role
import kr.bgmsound.bgmlab.output.provider.TokenProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtTokenProvider(
    @Value("\${token.secret}") private val secret: String,
    @Value("\${token.expiration.access}") private val accessTokenExpiration: Long,
    @Value("\${token.expiration.refresh}") private val refreshTokenExpiration: Long
) : TokenProvider {

    private val signKey: SecretKey = SecretKeySpec(secret.toByteArray(), "HmacSHA256")

    override fun createAccessToken(id: String, roles: List<Role>): String {
        return Jwts
            .builder()
            .header().empty().add(buildHeader()).and()
            .claims(payload(id, roles))
            .expiration(buildAccessTokenExpiration())
            .issuedAt(Date())
            .signWith(signKey)
            .compact()
    }

    override fun createRefreshToken(id: String, roles: List<Role>): String {
        return Jwts
            .builder()
            .header().empty().add(buildHeader()).and()
            .claims(payload(id, roles))
            .expiration(buildRefreshTokenExpiration())
            .issuedAt(Date())
            .signWith(signKey)
            .compact()
    }

    override fun extractIdFromToken(token: String): String {
        return Jwts
            .parser()
            .verifyWith(signKey)
            .build()
            .parseSignedClaims(token)
            .payload["id"]
            .toString()
    }

    override fun extractRolesFromToken(token: String): List<Role> {
        return Jwts
            .parser()
            .verifyWith(signKey)
            .build()
            .parseSignedClaims(token)
            .payload["roles"]
            .toString()
            .split(", ")
            .map { Role.valueOf(it) }
    }

    private fun buildHeader() = mapOf(
        "typ" to "JWT",
        "alg" to "HS256",
        "regDate" to System.currentTimeMillis()
    )

    private fun payload(id: String, roles: List<Role>) = mapOf(
        "id" to id,
        "roles" to roles.joinTo(StringBuilder(), ", ") { it.name }
    )

    private fun buildAccessTokenExpiration() = Date(System.currentTimeMillis() + accessTokenExpiration * 1000)

    private fun buildRefreshTokenExpiration() = Date(System.currentTimeMillis() + refreshTokenExpiration * 1000)
}