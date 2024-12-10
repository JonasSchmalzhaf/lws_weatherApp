package schwarz.it.lws.weatherapp.repository

import org.springframework.data.jpa.repository.JpaRepository
import schwarz.it.lws.weatherapp.model.WeatherData
import java.time.LocalDateTime

interface WeatherRepository : JpaRepository<WeatherData, Long> {
    fun findByCity(city: String): List<WeatherData>
}