package schwarz.it.lws.weatherapp.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.web.client.RestTemplateBuilder
import schwarz.it.lws.weatherapp.model.WeatherData
import schwarz.it.lws.weatherapp.model.WeatherForecastModel
import schwarz.it.lws.weatherapp.repository.WeatherRepository
import java.time.LocalDate
import kotlin.test.assertEquals

class WeatherServiceTest {
    private val testForecast = WeatherForecastModel.WeatherForecast(
        cod = "Test",
        message = 0,
        cnt = 0,
        list = listOf(
            WeatherForecastModel.HourForecast(
                dt = 0L,
                main = WeatherForecastModel.Main(
                    temp = 0.0,
                    feels_like = 0.0,
                    temp_min = 0.0,
                    temp_max = 0.0,
                    pressure = 0,
                    sea_level = 0,
                    humidity = 0,
                    temp_kf = 0.0,
                    grnd_level = 0
                ),
                weather = listOf(
                    WeatherForecastModel.Weather(
                        id = 0,
                        main = "Test",
                        description = "Test",
                        icon = "Test"
                    )
                ),
                dt_txt = "2024-12-12",
            )
        ),
        city = WeatherForecastModel.City(name = "Heilbronn")
    )

    private val testData = listOf(
        WeatherData(
            id = 1,
            city = testForecast.city.name,
            forecastDate = LocalDate.of(
                testForecast.list[0].dt_txt.split("-")[0].toInt(),
                testForecast.list[0].dt_txt.split("-")[1].toInt(),
                testForecast.list[0].dt_txt.split("-")[2].toInt()
            ),
            temperature = testForecast.list[0].main.temp,
            minTemperatur = testForecast.list[0].main.temp_min,
            maxTemperature = testForecast.list[0].main.temp_max,
            humidity = testForecast.list[0].main.humidity,
            description = testForecast.list[0].weather[0].description,
            iconCode = testForecast.list[0].weather[0].icon,
        )
    )

    @Test
    fun `Should get weather for a given city`() {
        // Given
        val weatherRepository = mockk<WeatherRepository>()
        val restTemplateBuilder = mockk<RestTemplateBuilder>()
        val weatherService = WeatherService(weatherRepository, restTemplateBuilder)
        val city = "Heilbronn"

        // When
        every { weatherRepository.findByCity(city) } returns testData
        val data = weatherService.getWeatherForecast(city, 0)

        // Then
        assertThat(data).isEqualTo(testData)
    }

    @Test
    fun `Should get weather for a new city`() {
        // Given
        val weatherRepository = mockk<WeatherRepository>()
        val restTemplateBuilder = mockk<RestTemplateBuilder>()
        val weatherService = WeatherService(weatherRepository, restTemplateBuilder)
        val city = "Heilbronn"

        // When
        val apiKey = "0a796a8566000abaaf2677586ccb7571"
        val uri = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=metric"

        every { weatherRepository.findByCity(city) } returns listOf() andThen testData
        every { weatherRepository.save(any()) } returnsArgument 0
        every { weatherRepository.deleteById(any()) } returns Unit
        every {
            restTemplateBuilder.build().getForObject(uri, WeatherForecastModel.WeatherForecast::class.java)
        } returns testForecast

        val data = weatherService.getWeatherForecast(city, 0)

        // Then
        verify(exactly = 1) { weatherRepository.save(any()) }
        assertThat(data).isEqualTo(testData)
    }

    @Test
    fun `Should update weather if it's not up to date`() {
        // Given
        val weatherRepository = mockk<WeatherRepository>()
        val restTemplateBuilder = mockk<RestTemplateBuilder>()
        val weatherService = WeatherService(weatherRepository, restTemplateBuilder)
        val city = "Heilbronn"

        // When
        val apiKey = "0a796a8566000abaaf2677586ccb7571"
        val uri = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=metric"

        every { weatherRepository.findByCity(city) } returns testData
        every { weatherRepository.save(any()) } returnsArgument 0
        every { weatherRepository.findAll() } returns testData
        every { weatherRepository.deleteById(any()) } returns Unit
        every {
            restTemplateBuilder.build().getForObject(uri, WeatherForecastModel.WeatherForecast::class.java)
        } returns testForecast


        val data = weatherService.getWeatherForecast(city, 3)

        // Then
        verify(exactly = 1) { weatherRepository.save(any()) }
        verify(exactly = 1) { weatherRepository.findAll() }
        assertThat(data).isEqualTo(testData)
    }

    @Test
    fun `Should correctly refresh weather data`() {
        // Given
        val weatherRepository = mockk<WeatherRepository>()
        val restTemplateBuilder = mockk<RestTemplateBuilder>()
        val weatherService = WeatherService(weatherRepository, restTemplateBuilder)
        val city = "Heilbronn"

        // When
        val apiKey = "0a796a8566000abaaf2677586ccb7571"
        val uri = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=metric"

        every { weatherRepository.findAll() } returns testData
        every { weatherRepository.findByCity(city) } returns testData
        every { weatherRepository.save(any()) } returnsArgument 0
        every { weatherRepository.deleteById(any()) } returns Unit
        every {
            restTemplateBuilder.build().getForObject(uri, WeatherForecastModel.WeatherForecast::class.java)
        } returns testForecast

        weatherService.refreshWeatherForecastData()

        // Then
        verify(exactly = 1) { weatherRepository.deleteById(any()) }
    }

    @Test
    fun `Should correctly get forecast from OpenWeatherMap`() {
        // Given
        val weatherRepository = mockk<WeatherRepository>()
        val restTemplateBuilder = mockk<RestTemplateBuilder>()
        val weatherService = WeatherService(weatherRepository, restTemplateBuilder)
        val city = "Heilbronn"

        // When
        val apiKey = "0a796a8566000abaaf2677586ccb7571"
        val uri = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=metric"

        every { weatherRepository.findAll() } returns testData
        every { weatherRepository.findByCity(city) } returns testData
        every { weatherRepository.save(any()) } returnsArgument 0
        every { weatherRepository.deleteById(any()) } returns Unit
        every {
            restTemplateBuilder.build().getForObject(uri, WeatherForecastModel.WeatherForecast::class.java)
        } returns testForecast

        weatherService.getForecastFromOpenWeatherMap(city)

        // Then
        verify(exactly = 1) { weatherRepository.save(any()) }
    }


}