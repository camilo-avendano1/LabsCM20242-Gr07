package co.edu.udea.compumovil.labs20242_gr07

import android.util.Log
import androidx.collection.mutableObjectListOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.coroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CountryApi {
    private const val BASE_URL = "https://countryinfoapi.com/" // URL base para GeoNames

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val service: CountryApiService = retrofit.create(CountryApiService::class.java)

    @Composable
    fun getCountries(): List<Country>{
        var countries by remember {mutableStateOf(listOf<Country>())}
        LaunchedEffect(Unit) {
            coroutineScope{
                try {
                    countries = service.getCountries()
                } catch (e: Exception) {
                    Log.d("Error: ", e.toString())
                }
            }
        }
        return countries
    }

    @Composable
    fun getCca2(country:String): Country{
        var cca2 by remember { mutableStateOf(Country(country,"")) }
        LaunchedEffect(country) {
                try {
                    cca2 = service.getCca2(country)
                } catch (e: Exception) {
                    Log.d("Error: ", e.toString())
                }
        }
        return cca2
    }
}