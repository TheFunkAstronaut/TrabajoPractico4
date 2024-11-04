package com.example.trabajopractico4.ui.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

class EditContactActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextCompany: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextCity: EditText
    private lateinit var editTextState: EditText
    private lateinit var buttonUpdateContact: Button
    private lateinit var buttonDeleteContact: Button
    private val phoneLabels = listOf("Home", "Work", "University", "Custom")
    private val emailLabels = listOf("Personal", "Work", "University", "Custom")
    private val phoneList = mutableListOf<Phone>()
    private val emailList = mutableListOf<Email>()

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://apicontactos.jmacboy.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private var contactId: Int = 0  // To hold the ID of the contact being edited

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_contact)

        editTextName = findViewById(R.id.editTextName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextCompany = findViewById(R.id.editTextCompany)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextCity = findViewById(R.id.editTextCity)
        editTextState = findViewById(R.id.editTextState)
        buttonUpdateContact = findViewById(R.id.buttonUpdateContact)
        buttonDeleteContact = findViewById(R.id.buttonDeleteContact)

        // Get the contact ID from the Intent
        contactId = intent.getIntExtra("CONTACT_ID", 0)

        // Load the contact details
        loadContactDetails()

        buttonUpdateContact.setOnClickListener {
            updateContact()
        }

        buttonDeleteContact.setOnClickListener {
            deleteContact()
        }
    }

    private fun loadContactDetails() {
        apiService.getContact(contactId).enqueue(object : Callback<Contact> {
            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                if (response.isSuccessful) {
                    val contact = response.body()
                    contact?.let {
                        editTextName.setText(it.name)
                        editTextLastName.setText(it.last_name)
                        editTextCompany.setText(it.company)
                        editTextAddress.setText(it.address)
                        editTextCity.setText(it.city)
                        editTextState.setText(it.state)
                    }
                }
            }

            override fun onFailure(call: Call<Contact>, t: Throwable) {
                TODO()
            }
        })
    }

    private fun updateContact() {
        val name = editTextName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val company = editTextCompany.text.toString().trim()
        val address = editTextAddress.text.toString().trim()
        val city = editTextCity.text.toString().trim()
        val state = editTextState.text.toString().trim()

        val contact = Contact(
            id = contactId,
            name = name,
            last_name = lastName,
            company = company,
            address = address,
            city = city,
            state = state,
            profile_picture = "",
            phones = listOf(),
            emails = listOf()
        )

        apiService.updateContact(contactId, contact).enqueue(object : Callback<Contact> {
            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditContactActivity, "Contact updated successfully!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)  // Set result to OK
                    finish()  // Close the activity and return to the previous one
                } else {
                    Toast.makeText(this@EditContactActivity, "Failed to update contact", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Contact>, t: Throwable) {
                Toast.makeText(this@EditContactActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteContact() {
        apiService.deleteContact(contactId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditContactActivity, "Contact deleted successfully!", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)  // Set result to OK
                    finish()  // Close the activity and return to the previous one
                } else {
                    Toast.makeText(this@EditContactActivity, "Failed to delete contact", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@EditContactActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
