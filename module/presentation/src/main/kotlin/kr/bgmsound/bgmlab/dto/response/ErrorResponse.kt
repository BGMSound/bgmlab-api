package kr.bgmsound.bgmlab.dto.response

import kr.bgmsound.bgmlab.exception.code.ErrorCode

data class ErrorResponse(
    val code: String,
    val message: String
) {
    companion object {
        fun of(errorCode: ErrorCode): ErrorResponse {
            return ErrorResponse(errorCode.code, errorCode.message)
        }
    }
}