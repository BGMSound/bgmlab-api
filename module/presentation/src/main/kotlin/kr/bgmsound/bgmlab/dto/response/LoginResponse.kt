package kr.bgmsound.bgmlab.dto.response

import kr.bgmsound.bgmlab.model.Role
import org.springframework.http.HttpStatus

data class LoginResponse(
    val role: Role,
    val accessToken: String,
    val refreshToken: String
)