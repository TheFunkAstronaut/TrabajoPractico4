package com.example.trabajopractico4.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.trabajopractico4.R
import com.example.trabajopractico4.api.ApiService
import com.example.trabajopractico4.models.Contact
import com.example.trabajopractico4.models.Email
import com.example.trabajopractico4.models.Phone
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddContactActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextCompany: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextCity: EditText
    private lateinit var editTextState: EditText
    private lateinit var buttonSaveContact: Button

    private val phoneList = mutableListOf<Phone>()
    private val emailList = mutableListOf<Email>()


    private val apiService: ApiService by lazy {
        // Create Retrofit instance here
        Retrofit.Builder()
            .baseUrl("https://apicontactos.jmacboy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_contact)

        editTextName = findViewById(R.id.editTextName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextCompany = findViewById(R.id.editTextCompany)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextCity = findViewById(R.id.editTextCity)
        editTextState = findViewById(R.id.editTextState)
        buttonSaveContact = findViewById(R.id.buttonSaveContact)

        buttonSaveContact.setOnClickListener {
            saveContact()
        }
    }

    private fun saveContact() {
        val name = editTextName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val company = editTextCompany.text.toString().trim()
        val address = editTextAddress.text.toString().trim()
        val city = editTextCity.text.toString().trim()
        val state = editTextState.text.toString().trim()

        phoneList.clear()
        emailList.clear()

        if (name.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please enter a name and last name", Toast.LENGTH_SHORT).show()
            return
        }

        val contact = Contact(
            id = 0,
            name = name,
            last_name = lastName,
            company = company,
            address = address,
            city = city,
            state = state,
            profile_picture = "",
            phones = phoneList,
            emails = emailList
        )

        apiService.addContact(contact).enqueue(object : Callback<Contact> {
            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddContactActivity, "Contact added successfully!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)  // Set result to OK
                    finish()  // Close the activity and return to the previous one
                } else {
                    Toast.makeText(this@AddContactActivity, "Failed to add contact", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Contact>, t: Throwable) {
                Toast.makeText(this@AddContactActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
