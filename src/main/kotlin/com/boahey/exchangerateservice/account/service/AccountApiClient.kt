package com.boahey.exchangerateservice.account.service

import com.boahey.exchangerateservice.account.model.AccountInfo
import com.boahey.exchangerateservice.account.model.AccountType
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration

@Service
class AccountApiClient(
        @Value("\${account.api.url}")
        private val accountApiClientUrl: String,
        @Value("\${account.api.is.dummy.account.map}")
        private val isDummyAccountMap: Boolean

) {
    private val log = LoggerFactory.getLogger(AccountApiClient::class.java)

    private val exchangeRatesApiClient: WebClient by lazy {
        WebClient
                .builder()
                .baseUrl(accountApiClientUrl)
                .build()
    }
    private val accountInfoMap: Map<String, AccountInfo> = hashMapOf(
            "user1" to AccountInfo("user1", AccountType.FREE),
            "user2" to  AccountInfo("user2", AccountType.PREMIUM),
            "defaultUser" to  AccountInfo("defaultUser", AccountType.FREE)
    )

    fun getAccountInfo(customerId: String): Mono<AccountInfo> {
        val uri: String = calculateUrl(customerId)
        log.debug("uri: {}", uri)
        return if (isDummyAccountMap) {
            Mono.just(accountInfoMap[customerId] ?: AccountInfo("defaultUser", AccountType.FREE))
        } else
            getAccountServiceApiCall(uri)
    }

    fun getAccountInfoDummy(customerId: String): Mono<AccountInfo> {
        return Mono.just(AccountInfo("defaultUser", AccountType.FREE))
    }
    fun getAccountServiceApiCall(uri: String): Mono<AccountInfo> {
        return exchangeRatesApiClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(AccountInfo::class.java)
                .let { it }
                .timeout(Duration.ofSeconds(3))
//                .doOnError()
//                .doOnSuccess()
    }

    private fun calculateUrl(customerId: String): String {
        return accountApiClientUrl + "api/users/" + customerId
    }

}