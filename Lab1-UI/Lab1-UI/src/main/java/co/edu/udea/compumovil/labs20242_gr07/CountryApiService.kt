package co.edu.udea.compumovil.labs20242_gr07

import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApiService {
    @GET("/api/countries/")
    suspend fun getCountries(): List<Country>

    @GET("/api/countries/name/{name}")
    suspend fun getCca2(@Path("name") name:String): Country
}

data class Country(
    val name: String,
    val cca2: String
)