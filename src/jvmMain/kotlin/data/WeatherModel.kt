package data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class WeatherResponse(
    val main:Main,
    val weather:List<Weather>,
    val coord:Coord,
)

@Serializable
data class WeatherResponse2(
    val hourly: List<Forecast>
)
@Serializable
data class Main(
    @SerialName("temp") val tempC: Double,
    @SerialName("feels_like") val feelslikeC: Double,
    @SerialName("humidity") val humidity:Double
)
@Serializable
data class Weather(
    @SerialName("description") val description: String,
    @SerialName("icon") val icon: String

)

@Serializable
data class Forecast(
    @SerialName("temp") val tempC: Double,
    @SerialName("feels_like") val feelslikeC: Double,
    val weather:List<Weather>,
)


@Serializable
data class Coord(
    @SerialName("lon") val lon: Double,
    @SerialName("lat") val lat: Double,
)

