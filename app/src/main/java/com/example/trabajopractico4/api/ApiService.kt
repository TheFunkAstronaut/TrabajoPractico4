package com.example.trabajopractico4.api

import com.example.trabajopractico4.models.Contact
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @GET("api/personas")
    fun getContacts(): Call<List<Contact>>

    @POST("api/personas")
    fun addContact(@Body contact: Contact): Call<Contact>

    @PUT("api/personas/{id}")
    fun updateContact(@Path("id") id: Int, @Body contact: Contact): Call<Contact>

    @DELETE("api/personas/{id}")
    fun deleteContact(@Path("id") id: Int): Call<Void>

    @GET("api/personas/{id}")
    fun getContact(@Path("id") id: Int): Call<Contact>

}

