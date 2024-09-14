package co.edu.udea.compumovil.labs20242_gr07

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityApiService {
    @GET("/v1/city")
    suspend fun getCities(
        @Header("X-Api-Key") apiKey: String,
        @Query("country") cca2: String,
        @Query("limit") limit: Int = 30
    ): List<City>
}

data class City(
    val name: String
)

object CityApi {
    private const val BASE_URL = "https://api.api-ninjas.com/"
    private const val API_KEY = "0hHpRqhPDq/F6mlEhhG8KA==rTgcJY2kQqflDWCY"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val cityService: CityApiService = retrofit.create(CityApiService::class.java)

    @Composable
    fun getCities(cca2: String): List<City> {
        var cities by remember { mutableStateOf(listOf<City>()) }
        LaunchedEffect(cca2) {
            try {
                cities = cityService.getCities(API_KEY, cca2)
            } catch (e: Exception) {
                Log.d("Error: ", e.toString())
            }
        }
        return cities
    }
}