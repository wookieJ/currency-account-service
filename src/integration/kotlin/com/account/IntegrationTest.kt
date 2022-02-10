package com.account

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.event.annotation.AfterTestExecution
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import javax.servlet.http.HttpServletResponse.SC_OK

@SpringBootTest(
    classes = [AppRunner::class],
    properties = ["application.environment=integration"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class IntegrationTest {

    val restTemplate: RestTemplate = RestTemplate()

    companion object {
        private val nbpWebApiMock = WireMockServer(WireMockConfiguration.options().dynamicPort().notifier(ConsoleNotifier(true))).apply { start() }

        @DynamicPropertySource
        @JvmStatic
        fun registerDynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("clients.nbp.url") { "http://localhost:${nbpWebApiMock.port()}" }
        }

        @AfterTestExecution
        @JvmStatic
        fun cleanup() {
            nbpWebApiMock.stop()
        }
    }

    @LocalServerPort
    var port: Int = 0

    val serverUrl by lazy { "http://localhost:$port" }

    @BeforeEach
    fun setup() {
        nbpWebApiMock.resetAll()
    }

    fun stubGetUSDExchangeRateToday() {
        nbpWebApiMock.stubFor(
            WireMock.get("/api/exchangerates/rates/C/USD/today")
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(SC_OK)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                            """
                            {
                                "table": "C",
                                "currency": "dolar amerykański",
                                "code": "USD",
                                "rates": [ 
                                    {
                                        "no": "030/C/NBP/2022",
                                        "effectiveDate": "2022-02-14",
                                        "bid": 3.9374,
                                        "ask": 4.017
                                    }
                                ]
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    fun stubGetUSDExchangeRateTodayNotFound() {
        nbpWebApiMock.stubFor(
            WireMock.get("/api/exchangerates/rates/C/USD/today")
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(SC_NOT_FOUND)
                        .withHeader("Content-Type", "text/plain")
                        .withBody("Brak danych")
                )
        )
    }
}
