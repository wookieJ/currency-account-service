package com.account

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.stubbing.Scenario
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.event.annotation.AfterTestExecution
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate
import javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR
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
            registry.add("exchange.clients.nbp.url") { "http://localhost:${nbpWebApiMock.port()}" }
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

    fun stubGetUSDExchangeRate(effectiveDate: LocalDate, bid: BigDecimal, ask: BigDecimal) {
        nbpWebApiMock.stubFor(
            WireMock.get("/api/exchangerates/rates/C/USD/last")
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
                                        "effectiveDate": "$effectiveDate",
                                        "bid": $bid,
                                        "ask": $ask
                                    }
                                ]
                            }
                            """.trimIndent()
                        )
                )
        )
    }

    fun stubGetUSDExchangeRateFlappingResponse(effectiveDate: LocalDate, bid: BigDecimal, ask: BigDecimal) {
        val scenarioName = "First request 500, second 200"
        nbpWebApiMock.stubFor(
            WireMock.get("/api/exchangerates/rates/C/USD/last")
                .inScenario(scenarioName)
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(SC_INTERNAL_SERVER_ERROR)
                        .withHeader("Content-Type", "text/plain")
                )
                .willSetStateTo("SECOND_TRY")
        )

        nbpWebApiMock.stubFor(
            WireMock.get("/api/exchangerates/rates/C/USD/last")
                .inScenario(scenarioName)
                .whenScenarioStateIs("SECOND_TRY")
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
                                        "effectiveDate": "$effectiveDate",
                                        "bid": $bid,
                                        "ask": $ask
                                    }
                                ]
                            }
                            """.trimIndent()
                        )
                )
        )
    }
}
