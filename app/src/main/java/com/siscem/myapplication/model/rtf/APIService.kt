package com.siscem.myapplication.model.rtf

import com.siscem.myapplication.model.gsonConvert.Person
import com.siscem.myapplication.model.PoolDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {

    @Headers("Accept: application/json")
    @POST("data/clients.php")
    fun downloadPeople(@Body pool: PoolDTO): Call<Person>
}