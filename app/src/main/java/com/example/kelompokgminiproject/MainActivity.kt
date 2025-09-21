package com.example.kelompokgminiproject

import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val searchView = findViewById<SearchView>(R.id.searchView)

        // Default -> tampilakn welcome fragmentynya
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, WelcomeFragment())
            .commit()

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val fragment = SearchResultFragment().apply {
                        arguments = Bundle().apply {
                            putString("query", query)
                        }
                    }

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, WelcomeFragment())
                        .commit()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, WelcomeFragment())
                        .commit()
                }

                return true
            }
        })
    }
}