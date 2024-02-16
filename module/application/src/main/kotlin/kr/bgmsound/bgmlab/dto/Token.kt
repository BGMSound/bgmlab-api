package kr.bgmsound.bgmlab.dto

data class Token(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun of(accessToken: String, refreshToken: String) = Token(accessToken, refreshToken)
    }
}