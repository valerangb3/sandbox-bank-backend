package service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.JwtConfig
import com.example.domain.model.LoginUser
import domain.model.auth.JwtTokens
import io.ktor.server.application.ApplicationEnvironment
import java.util.Date

class JwtService(private val environment: ApplicationEnvironment) {
    fun genTokens(user: LoginUser): JwtTokens {
        return JwtTokens(
            accessToken = genAccessToken(user.login),
            refreshToken = genRefreshToken(user.login)
        )
    }

    private fun createToken(login: String, expireDate: Date): String {
        val jwtConfig = JwtConfig(environment.config)
        val token = JWT.create()
            .withAudience(jwtConfig.audience)
            .withIssuer(jwtConfig.issuer)
            .withClaim("login", login)
            .withExpiresAt(expireDate)
            .sign(Algorithm.HMAC256(jwtConfig.secret))
        return token
    }

    private fun genAccessToken(login: String): String {
        return createToken(login, Date(System.currentTimeMillis() + ACCESS_EXPIRE_IN))
    }

    private fun genRefreshToken(login: String): String {
        return createToken(login, Date(System.currentTimeMillis() + REFRESH_EXPIRE_IN))
    }

    companion object {
        private const val ACCESS_EXPIRE_IN = 60_000
        private const val REFRESH_EXPIRE_IN = 600_000
    }
}