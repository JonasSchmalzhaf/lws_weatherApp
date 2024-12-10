package schwarz.it.lws.weatherapp.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import schwarz.it.lws.weatherapp.model.WeatherData
import schwarz.it.lws.weatherapp.model.WeatherForecastModel
import schwarz.it.lws.weatherapp.repository.WeatherRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class WeatherService(
    private val weatherRepository: WeatherRepository,
    private val restTemplateBuilder: RestTemplateBuilder
) {
    fun getWeatherForecast(city: String, days: Int): List<WeatherData> {
        getForecastFromOpenWeatherMap("Heilbronn")
        return weatherRepository.findByCity(city).filter { LocalDateTime.now() <= it.forecastDate && it.forecastDate <= LocalDateTime.now().plusDays(days.toLong()) }
    }

    fun getForecastFromOpenWeatherMap(city: String): List<WeatherData> {
        val apiKey = "0a796a8566000abaaf2677586ccb7571"
        val uri = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=metric"

        val openWeatherMap = restTemplateBuilder.build()
        val receivedForecast = openWeatherMap.getForObject(uri, WeatherForecastModel.WeatherForecast::class.java)


        receivedForecast!!.list.forEach { hour -> weatherRepository.save(
            WeatherData(
                city = receivedForecast.city.name,
                forecastDate = LocalDateTime.ofEpochSecond(hour.dt, 0, ZoneOffset.UTC),
                temperature = hour.main.temp,
                minTemperatur = hour.main.temp_min,
                maxTemperature = hour.main.temp_max,
                humidity = hour.main.humidity,
                description = hour.weather.description,
                iconCode = hour.weather.icon
            )
        )}

        return listOf()
    }
}