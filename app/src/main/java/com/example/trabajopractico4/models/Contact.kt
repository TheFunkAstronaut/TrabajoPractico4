package com.example.trabajopractico4.models

data class Contact(
    val id: Int,
    val name: String,
    val last_name: String,
    val company: String,
    val address: String,
    val city: String,
    val state: String,
    val profile_picture: String,
    val phones: List<Phone>,
    val emails: List<Email>
)

data class Phone(
    val id: Int,
    val number: String,
    val persona_id: Int,
    val label: String
)

data class Email(
    val id: Int,
    val email: String,
    val persona_id: Int,
    val label: String
)
