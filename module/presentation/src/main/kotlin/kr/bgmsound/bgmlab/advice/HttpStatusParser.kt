package kr.bgmsound.bgmlab.advice

import kr.bgmsound.bgmlab.error.ErrorCode
import org.springframework.http.HttpStatus

fun ErrorCode.httpStatus() = when (this) {
    ErrorCode.UNKNOWN_ERROR -> HttpStatus.BAD_REQUEST.value()
    ErrorCode.NOT_SIGNUP -> HttpStatus.UNAUTHORIZED.value()
    ErrorCode.NOT_AUTHORIZED -> HttpStatus.UNAUTHORIZED.value()
    ErrorCode.AUTHENTICATION_FAIL -> HttpStatus.UNAUTHORIZED.value()
    ErrorCode.TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED.value()
    ErrorCode.INVALID_ERROR -> HttpStatus.BAD_REQUEST.value()
    ErrorCode.USER_NOT_FOUND -> HttpStatus.NOT_FOUND.value()
}