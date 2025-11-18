package service

import com.example.domain.model.LoginUser
import com.example.repository.UserRepository
import com.example.repository.model.User
import domain.model.auth.JwtTokens
import repository.TokenRepository

class UserService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository<JwtTokens>
) {
    suspend fun getUserByLogin(login: String): User? {
        val user = userRepository.userByLogin(login)
        return user
    }

    suspend fun refreshTokens(loginUser: LoginUser): JwtTokens {
        val jwtTokens = tokenRepository.refreshToken(loginUser)

        tokenRepository.refreshToken(loginUser)
        return jwtTokens.data
    }
}