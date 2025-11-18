package repository

import com.example.domain.model.LoginUser
import com.example.repository.UserRepository
import domain.model.auth.JwtTokens
import domain.model.auth.Tokens
import service.JwtService

class JwtTokenRepositoryImpl(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) : TokenRepository<JwtTokens> {
    override suspend fun refreshToken(user: LoginUser): Tokens<JwtTokens> {
        val jwtTokens = jwtService.genTokens(user)
        updateUserTokens(user, jwtTokens)
        val tokenBox = Tokens(jwtTokens)
        return tokenBox
    }

    override suspend fun getUserByToken(token: String): String? {
        return userRepository.userByToken(token)?.login
    }

    private suspend fun updateUserTokens(user: LoginUser, jwtTokens: JwtTokens) {
        //TODO доделать реализацию
    }
}