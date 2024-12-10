package schwarz.it.lws.weatherapp.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import schwarz.it.lws.weatherapp.model.WeatherData
import schwarz.it.lws.weatherapp.repository.WeatherRepository
import java.time.LocalDateTime

@Service
class WeatherService(private val weatherRepository: WeatherRepository) {
    fun getWeatherForecast(city: String,  days: Int): List<WeatherData> {
        return weatherRepository.findByCityName(city).filter { LocalDateTime.now() <= it.forecastDate && it.forecastDate <= LocalDateTime.now().plusDays(days.toLong()) }
    }
}