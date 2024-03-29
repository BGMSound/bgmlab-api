package kr.bgmsound.bgmlab.authentication

import kr.bgmsound.bgmlab.authentication.dto.AuthenticationDto
import kr.bgmsound.bgmlab.model.User

interface AuthenticationStrategy {

    fun authenticate(authentication: AuthenticationDto): User

}