package kr.bgmsound.bgmlab.persistence.repository.impl

import kr.bgmsound.bgmlab.authentication.repository.UserNativeAccountRepository
import kr.bgmsound.bgmlab.authentication.dto.NativeAccount
import org.springframework.stereotype.Repository

@Repository
class UserNativeAccountRepositoryImpl : UserNativeAccountRepository {

    override fun findByDisplayId(displayId: String): NativeAccount? {
        TODO("Not yet implemented")
    }

    override fun save(account: NativeAccount) {
        TODO("Not yet implemented")
    }
}