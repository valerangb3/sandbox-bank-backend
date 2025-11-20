package repository

import com.example.repository.UserRepository
import com.example.repository.db.suspendTransaction
import domain.model.auth.JwtTokens
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.update
import repository.db.UserTable

class JwtTokenRepositoryImpl(
    private val userRepository: UserRepository,
) : TokenRepository {
    override suspend fun saveToken(login: String, tokens: JwtTokens) {
        suspendTransaction {
            UserTable.update({ UserTable.login eq login }) {
                it[accessToken] = tokens.accessToken
                it[refreshToken] = tokens.refreshToken
            }
        }
    }

    override suspend fun getUserByToken(token: String): String? {
        return userRepository.userByToken(token)?.login
    }
}