package data

import io.ktor.client.*
import io.ktor.client.request.*

class WeatherTransformer{



    fun transform(response: WeatherResponse, response2: WeatherResponse2): WeatherResults {
        val current = extractCurrentWeatherFrom(response)
        val forecast = extractForecastWeatherFrom(response2)

        return WeatherResults(
            currentWeather = current,
           forecast = forecast,
        )
    }

    private fun extractCurrentWeatherFrom(response: WeatherResponse): WeatherCard {
        return WeatherCard(
            condition = response.weather[0].description,
            iconUrl = "http://openweathermap.org/img/wn/${response.weather[0].icon}@2x.png" ,
            temperature = response.main.tempC,
            feelsLike = response.main.feelslikeC,
            humidity = response.main.humidity
        )
    }

    private fun extractForecastWeatherFrom(response: WeatherResponse2): List<WeatherCard> {
        return response.hourly.map { forecastDay ->
            WeatherCard(
                condition = forecastDay.weather[0].description,
                iconUrl = "http://openweathermap.org/img/wn/${forecastDay.weather[0].icon}@2x.png",
                temperature = forecastDay.tempC,
                feelsLike = forecastDay.feelslikeC,
                //humidity = forecastDay.weather[0].
            )
        }
    }

}