package schwarz.it.lws.weatherapp.service

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import schwarz.it.lws.weatherapp.model.WeatherData
import schwarz.it.lws.weatherapp.model.WeatherForecastModel
import schwarz.it.lws.weatherapp.repository.WeatherRepository
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class WeatherService(private val weatherRepository: WeatherRepository, private val restTemplateBuilder: RestTemplateBuilder) {
    fun getWeatherForecast(city: String, days: Int): List<WeatherData> {
        if (weatherRepository.findByCity(city).isEmpty()) {
            getForecastFromOpenWeatherMap(city)
        }

        return weatherRepository.findByCity(city).filter { LocalDate.now() <= it.forecastDate && it.forecastDate <= LocalDate.now().plusDays(days.toLong()) }
    }

    @Scheduled(cron = "0 * /3 * * *")
    fun refreshWeatherForecastData() {
        val cities = mutableSetOf<String>()

        weatherRepository.findAll().forEach { weather -> cities.add(weather.city) }

        weatherRepository.deleteAll()

        cities.forEach { city -> getForecastFromOpenWeatherMap(city) }
    }

    fun getForecastFromOpenWeatherMap(city: String) {
        val apiKey = "0a796a8566000abaaf2677586ccb7571"
        val uri = "https://api.openweathermap.org/data/2.5/forecast?q=$city&appid=$apiKey&units=metric"

        val openWeatherMap = restTemplateBuilder.build()
        val receivedForecast = openWeatherMap.getForObject(uri, WeatherForecastModel.WeatherForecast::class.java)

        forecastToDaily(receivedForecast!!).forEach { weather -> weatherRepository.save(weather) }
    }

    fun forecastToDaily(forecast: WeatherForecastModel.WeatherForecast): List<WeatherData> {
        val groupedList = forecast.list.groupBy { it.dt_txt.split(" ")[0] }
        val dailyForecast = mutableListOf<WeatherData>()

        groupedList.forEach { (date, dayForecast) -> dailyForecast.add(WeatherData(
            city = forecast.city.name,
            forecastDate = LocalDate.of(date.split("-")[0].toInt(), date.split("-")[1].toInt(), date.split("-")[2].toInt()),
            temperature = dayForecast.sumOf { it.main.temp } / dayForecast.size,
            minTemperatur = dayForecast.minOf { it.main.temp_min },
            maxTemperature = dayForecast.maxOf { it.main.temp_max },
            humidity = dayForecast.sumOf { it.main.humidity } / dayForecast.size,
            description = dayForecast.groupingBy { it.weather.first().description }.eachCount().maxBy { it.value }.key,
            iconCode = dayForecast.groupBy { it.weather.first().icon }.keys.joinToString("\n")
        ))}

        return dailyForecast
    }
}