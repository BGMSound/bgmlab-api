package kr.bgmsound.bgmlab.output.provider

import kr.bgmsound.bgmlab.model.Role

interface TokenProvider {

    fun createAccessToken(id: String, roles: List<Role>): String

    fun createRefreshToken(id: String, roles: List<Role>): String

    fun extractIdFromToken(token: String): String

    fun extractRolesFromToken(token: String): List<Role>

}