package schwarz.it.lws.weatherapp.model

class WeatherForecastModel {
    data class Main(
        val temp: Double,
        val feels_like: Double,
        val temp_min: Double,
        val temp_max: Double,
        val pressure: Int,
        val sea_level: Int,
        val grnd_level: Int,
        val humidity: Int,
        val temp_kf: Double,
    )

    data class Weather(
        val id: Int,
        val main: String,
        val description: String,
        val icon: String
    )

    data class HourForecast(
        val dt: Long,
        val main: Main,
        val weather: List<Weather>,
        val dt_txt: String,
    )

    data class City(
        val name: String,
    )

    data class WeatherForecast(
        val cod: String,
        val message: Int,
        val cnt: Int,
        val list: List<HourForecast>,
        val city: City
    )
}