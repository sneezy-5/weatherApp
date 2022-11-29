package data


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.*

class Repository(
    private val apiKey: String,
    private val client: HttpClient = defaultHttpClient,
) {
    private val transformer = WeatherTransformer()

    private suspend fun getWeatherForCity(city: String): WeatherResponse =
        client.get(
            "http://api.openweathermap.org/data/2.5/weather?q=${city}&lang=fr&units=metric&appid=${apiKey}"

        )


    private suspend fun getWeatherForestcastForCity(lon: Double, lat:Double): WeatherResponse2 =
        client.get(
            "https://api.openweathermap.org/data/2.5/onecall?lat=$lat&lon=$lon&lang=fr&exclude=munitely&units=metric&appid=${apiKey}"

        )


    @OptIn(DelicateCoroutinesApi::class)
    suspend fun weatherForCity(city: String): Lce<WeatherResults> {
        return try {

            val result = GlobalScope.async  {
                getWeatherForCity(city)
            }
            val result2 = GlobalScope.async  {
               // delay(1000L)
              getWeatherForestcastForCity(result.await().coord.lon, result.await().coord.lat)

            }
           result.join()
            val content = transformer.transform(result.await(), result2.await())

            Lce.Content(content)
        } catch (e: Exception) {
            e.printStackTrace()
            Lce.Error(e)


        }
    }

    companion object {
        val defaultHttpClient = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    json = kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}