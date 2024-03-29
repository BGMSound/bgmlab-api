package kr.bgmsound.bgmlab.advice.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.bgmsound.bgmlab.advice.httpStatus
import kr.bgmsound.bgmlab.advice.security.APIAuthentication
import kr.bgmsound.bgmlab.dto.response.ErrorResponse
import kr.bgmsound.bgmlab.error.APIException
import kr.bgmsound.bgmlab.error.ErrorCode
import kr.bgmsound.bgmlab.authentication.TokenProvider
import kr.bgmsound.bgmlab.util.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomJwtFilter(
    @Value("\${app.headers.auth-token}") private val authTokenHeader: String,
    private val tokenProvider: TokenProvider,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    private val log = getLogger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getHeader(authTokenHeader)
        if(token == null) {
            filterChain.doFilter(request, response)
            return
        }
        try {
            val authentication = makeAuthentication(token)
            registerAuthentication(authentication)
        } catch (exception: APIException) {
            log.error(exception.message)
            writeErrorResponse(response, exception.errorCode)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun registerAuthentication(authentication: Authentication) {
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun makeAuthentication(token: String): Authentication {
        return APIAuthentication.of(
            userId = tokenProvider.extractIdFromToken(token),
            accessToken = token,
            roles = tokenProvider.extractRolesFromToken(token)
                .map {
                    SimpleGrantedAuthority("ROLE_${it}")
                }
                .toMutableList()
        )
    }

    private fun writeErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = errorCode.httpStatus()
        response.writer.write(
            objectMapper.writeValueAsString(ErrorResponse.of(errorCode))
        )
    }
}