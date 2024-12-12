package schwarz.it.lws.weatherapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import schwarz.it.lws.weatherapp.model.WeatherData
import schwarz.it.lws.weatherapp.service.WeatherService
import java.time.LocalDate

@WebMvcTest(WeatherController::class)
class WeatherControllerTest() {

    @TestConfiguration
    class TestConfig {
        @Bean
        fun weatherService() = mockk<WeatherService>()
    }

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var weatherService: WeatherService

    @Test
    fun `should return weather list`() {
        // Given
        val testData = listOf(
            WeatherData(
                city = "Heilbronn",
                forecastDate = LocalDate.now(),
                temperature = 0.00,
                minTemperatur = 0.00,
                maxTemperature = 0.00,
                humidity = 0,
                description = "test",
                iconCode = "test",
            )
        )

        every { weatherService.getWeatherForecast("Heilbronn", 0) } returns testData

        // When & Then

        mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/Heilbronn?days=0"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].temperature").value(testData[0].temperature))

        // Then


    }

}