package com.example.trabajopractico4.repositories

import com.example.trabajopractico4.api.ApiService
import com.example.trabajopractico4.models.Contact
import retrofit2.Call

class ContactRepository {
    private val apiService = RetrofitRepository.retrofit.create(ApiService::class.java)

    fun getContacts(): Call<List<Contact>> = apiService.getContacts()

    fun addContact(contact: Contact): Call<Contact> = apiService.addContact(contact)

    fun updateContact(id: Int, contact: Contact): Call<Contact> = apiService.updateContact(id, contact)
}
