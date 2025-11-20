package service

import com.example.domain.model.LoginUser
import com.example.repository.UserRepository
import com.example.repository.model.User
import repository.TokenRepository

class AuthService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val jwtService: JwtService
) {

    // Т.к. AuthService это общий комопнент который управляет аутентификацией
    // То реализацию обновление токена добавил в эту функцию
    // По сути она должна пересоздать токены и сохранить их в бд
    suspend fun refreshTokens(refreshToken: String): AuthResult {
        val login = tokenRepository.getUserByToken(refreshToken)
        return login?.let { curLogin ->
            val jwtTokens = jwtService.genTokens(curLogin)
            tokenRepository.saveToken(curLogin, jwtTokens)
            AuthResult.Success(jwtTokens)
        } ?: AuthResult.Error(AuthError.USER_NOT_EXIST)
    }

    suspend fun authenticate(loginUser: LoginUser): AuthResult {
        val user = userRepository.userByLogin(loginUser.login)
        return user?.let { curUser ->
            val isEquals = userRepository.isPasswordEquals(loginUser.password, curUser)
            if (isEquals) {
                val jwtTokens = jwtService.genTokens(curUser.login)
                tokenRepository.saveToken(curUser.login, jwtTokens)
                AuthResult.Success(jwtTokens)
            } else {
                AuthResult.Error(AuthError.INVALID_PASSWORD)
            }
        } ?: AuthResult.Error(AuthError.USER_NOT_EXIST)
    }
}