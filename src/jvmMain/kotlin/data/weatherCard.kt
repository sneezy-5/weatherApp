package data


data class WeatherCard(
    val condition: String,
    val iconUrl: String?=null,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Double? = null,
)

data class WeatherResults(
    val currentWeather: WeatherCard,
    val forecast: List<WeatherCard>,
)


