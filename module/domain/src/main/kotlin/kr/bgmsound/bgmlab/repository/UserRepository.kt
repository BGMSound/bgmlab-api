package kr.bgmsound.bgmlab.repository

import kr.bgmsound.bgmlab.model.User

interface UserRepository {

    fun findById(id: String): User?

    fun save(user: User)
}