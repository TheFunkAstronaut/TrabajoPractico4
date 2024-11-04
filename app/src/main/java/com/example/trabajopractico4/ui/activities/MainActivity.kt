package com.example.trabajopractico4.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
    private val EDIT_CONTACT_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewContacts.adapter = contactAdapter

        // FAB to add a new contact
        binding.fabAddContact.setOnClickListener {
            startActivityForResult(Intent(this, AddContactActivity::class.java), ADD_CONTACT_REQUEST_CODE)
        }

        // On click listener for editing contacts
        contactAdapter.setOnContactClickListener { contact ->
            val intent = Intent(this, EditContactActivity::class.java)
            intent.putExtra("CONTACT_ID", contact.id)
            startActivityForResult(intent, EDIT_CONTACT_REQUEST_CODE)
        }

        // Load contacts initially
        loadContacts()

        // Set up SearchView listener for filtering contacts
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchContacts(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    loadContacts()  // Load the full list if search is cleared
                } else {
                    searchContacts(newText)
                }
                return true
            }
        })

    }

    // Load all contacts
    private fun loadContacts() {
        contactRepository.getContacts().enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    contactAdapter.submitList(response.body())
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                // Handle error
            }
        })
    }

    private fun searchContacts(query: String) {
        contactRepository.getContacts().enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    val filteredContacts = response.body()?.filter { contact ->
                        contact.name.contains(query, ignoreCase = true) ||
                                contact.last_name.contains(query, ignoreCase = true) ||
                                contact.phones.any { phone -> phone.number.contains(query) }
                    }
                    contactAdapter.submitList(filteredContacts)
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                // Handle failure if needed
            }
        })
    }


    // Handle result from AddContactActivity and EditContactActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == ADD_CONTACT_REQUEST_CODE || requestCode == EDIT_CONTACT_REQUEST_CODE) && resultCode == RESULT_OK) {
            loadContacts()
        }
    }
}



