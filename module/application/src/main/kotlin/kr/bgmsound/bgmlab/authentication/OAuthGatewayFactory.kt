package kr.bgmsound.bgmlab.authentication

import kr.bgmsound.bgmlab.authentication.gateway.OAuthGateway

interface OAuthGatewayFactory {

    fun of(provider: String): OAuthGateway

}