package com.example.trabajopractico4.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trabajopractico4.databinding.ActivityMainBinding
import com.example.trabajopractico4.models.Contact
import com.example.trabajopractico4.repositories.ContactRepository
import com.example.trabajopractico4.ui.adapters.ContactAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val contactRepository = ContactRepository()
    private val contactAdapter = ContactAdapter()
    private val ADD_CONTACT_REQUEST_CODE = 1
    private val EDIT_CONTACT_REQUEST_CODE = 2  // Define a request code for editing contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewContacts.adapter = contactAdapter

        binding.fabAddContact.setOnClickListener {
            startActivityForResult(Intent(this, AddContactActivity::class.java), ADD_CONTACT_REQUEST_CODE)
        }

        contactAdapter.setOnContactClickListener { contact ->
            val intent = Intent(this, EditContactActivity::class.java)
            intent.putExtra("CONTACT_ID", contact.id)  // Pass the contact ID to the EditContactActivity
            startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE)  // Start EditContactActivity
        }

        loadContacts()
    }

    private fun loadContacts() {
        contactRepository.getContacts().enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    contactAdapter.submitList(response.body())
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                TODO()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
            loadContacts()  // Refresh the list when a new contact is added
        } else if (requestCode == EDIT_CONTACT_REQUEST_CODE && resultCode == RESULT_OK) {
            loadContacts()  // Refresh the list when a contact is edited or deleted
        }
    }

}
