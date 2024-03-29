package kr.bgmsound.bgmlab.authentication

import kr.bgmsound.bgmlab.IdentifierGenerator
import kr.bgmsound.bgmlab.TxUtil.Companion.writeWithTransaction
import kr.bgmsound.bgmlab.authentication.dto.AuthenticationDto
import kr.bgmsound.bgmlab.model.Role
import kr.bgmsound.bgmlab.model.User
import kr.bgmsound.bgmlab.repository.UserRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class OAuthAuthenticationStrategy(
    private val oAuthGatewayFactory: OAuthGatewayFactory,
    private val identifierGenerator: IdentifierGenerator,
    private val userRepository: UserRepository,
    private val userSocialAccountRepository: UserSocialAccountRepository
) : AuthenticationStrategy {

    override fun authenticate(authentication: AuthenticationDto): User {
        val oAuthGateway = oAuthGatewayFactory.of(authentication.type)
        val result = oAuthGateway.authenticate(authentication.credentials)

        return writeWithTransaction {
            userSocialAccountRepository
                .findBySocialId(result.provider, result.socialId)
                ?.let { userRepository.findById(it.userId) }
                ?: registerNewUser(result)
        }
    }

    private fun registerNewUser(loginResult: OAuthResult): User {
        val user = createNewUser(loginResult.provider, loginResult.socialId)
        val account = SocialAccount.of(user = user, provider = loginResult.provider, socialId = loginResult.socialId)

        userRepository.save(user)
        userSocialAccountRepository.save(account)
        return user
    }

    private fun createNewUser(provider: AuthenticationType, id: String): User {
        val suffix = identifierGenerator.generateIdentifier()
        val displayId = "${provider.name}${id}-${suffix.subSequence(0, 11)}".lowercase()

        return User(
            id = identifierGenerator.generateIdentifier(),
            displayId = displayId,
            name = displayId,
            roles = listOf(Role.NEED_SIGNUP),
            createAt = LocalDateTime.now()
        )
    }
}