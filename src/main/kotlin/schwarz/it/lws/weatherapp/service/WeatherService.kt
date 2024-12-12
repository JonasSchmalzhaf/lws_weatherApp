package schwarz.it.lws.weatherapp.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import schwarz.it.lws.weatherapp.model.WeatherData
import schwarz.it.lws.weatherapp.model.WeatherForecastModel
import schwarz.it.lws.weatherapp.repository.WeatherRepository
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URLEncoder
import java.time.LocalDate
import java.util.*

@Service
class WeatherService(
    private val weatherRepository: WeatherRepository,
    private val restTemplateBuilder: RestTemplateBuilder
) {
    fun getWeatherForecast(city: String, days: Int): List<WeatherData> {
        val safeCity = city.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            .split(' ')
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
            .split("%20")
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }

        if (weatherRepository.findByCity(safeCity).isEmpty()) {
            getForecastFromOpenWeatherMap(safeCity)
        }

        if (weatherRepository.findByCity(safeCity).filter {
                LocalDate.now() <= it.forecastDate && it.forecastDate <= LocalDate.now().plusDays(days.toLong())
            }.size < days) {
            refreshWeatherForecastData()
        }

        return weatherRepository.findByCity(safeCity)
            .filter { LocalDate.now() <= it.forecastDate && it.forecastDate <= LocalDate.now().plusDays(days.toLong()) }
    }

    @Scheduled(cron = "0 * /3 * * *")
    fun refreshWeatherForecastData() {
        val cities = mutableSetOf<String>()

        weatherRepository.findAll().forEach { weather -> cities.add(weather.city) }

        cities.forEach { city -> getForecastFromOpenWeatherMap(city) }
    }

    fun getForecastFromOpenWeatherMap(city: String) {
        val apiKey = "0a796a8566000abaaf2677586ccb7571"
        val uri = "https://api.openweathermap.org/data/2.5/forecast?q=${city}&appid=$apiKey&units=metric"

        try {
            val openWeatherMap = restTemplateBuilder.build()
            val receivedForecast = openWeatherMap.getForObject(
                uri,
                WeatherForecastModel.WeatherForecast::class.java
            )

            weatherRepository.findByCity(city).filter { it.forecastDate > LocalDate.now().minusDays(1) }
                .forEach { weather -> weatherRepository.deleteById(weather.id!!) }

            forecastToDaily(receivedForecast!!).forEach { weather ->
                weatherRepository.save(weather)
            }
        } catch (error: Exception) {
            println(city)
            println(error.message)
        }
    }

    fun forecastToDaily(forecast: WeatherForecastModel.WeatherForecast): List<WeatherData> {
        val groupedList = forecast.list.groupBy { it.dt_txt.split(" ")[0] }
        val dailyForecast = mutableListOf<WeatherData>()

        groupedList.forEach { (date, dayForecast) ->
            dailyForecast.add(
                WeatherData(
                    city = forecast.city.name,
                    forecastDate = LocalDate.of(
                        date.split("-")[0].toInt(),
                        date.split("-")[1].toInt(),
                        date.split("-")[2].toInt()
                    ),
                    temperature = BigDecimal(dayForecast.sumOf { it.main.temp } / dayForecast.size).setScale(
                        1,
                        RoundingMode.HALF_EVEN
                    ).toDouble(),
                    minTemperatur = BigDecimal(dayForecast.minOf { it.main.temp_min }).setScale(
                        1,
                        RoundingMode.HALF_EVEN
                    ).toDouble(),
                    maxTemperature = BigDecimal(dayForecast.maxOf { it.main.temp_max }).setScale(
                        1,
                        RoundingMode.HALF_EVEN
                    ).toDouble(),
                    humidity = dayForecast.sumOf { it.main.humidity } / dayForecast.size,
                    description = if (dayForecast.filter { it.weather.first().icon.contains("d") }
                            .isEmpty()) dayForecast.groupingBy { it.weather.first().description }.eachCount()
                        .maxBy { it.value }.key else dayForecast.filter { it.weather.first().icon.contains("d") }
                        .groupingBy { it.weather.first().description }.eachCount().maxBy { it.value }.key,
                    iconCode = if (dayForecast.filter { it.weather.first().icon.contains("d") }
                            .isEmpty()) dayForecast.groupingBy { it.weather.first().icon }.eachCount()
                        .maxBy { it.value }.key else dayForecast.filter { it.weather.first().icon.contains("d") }
                        .groupingBy { it.weather.first().icon }.eachCount().maxBy { it.value }.key,
                )
            )
        }

        return dailyForecast
    }
}