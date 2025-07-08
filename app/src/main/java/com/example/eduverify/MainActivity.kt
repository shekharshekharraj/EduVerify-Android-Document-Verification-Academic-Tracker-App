package com.example.eduverify

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eduverify.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnUpload.setOnClickListener {
            navigateTo(UploadActivity::class.java)
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            navigateTo(LoginActivity::class.java, finishCurrent = true)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Already on Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_docs -> {
                    navigateTo(UploadActivity::class.java)
                    true
                }
                R.id.nav_profile -> {
                    navigateTo(ProfileActivity::class.java)
                    true
                }
                R.id.nav_edit_profile -> {
                    navigateTo(EditProfileActivity::class.java)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateTo(destination: Class<*>, finishCurrent: Boolean = false) {
        val intent = Intent(this, destination)
        startActivity(intent)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        if (finishCurrent) finish()
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            navigateTo(LoginActivity::class.java, finishCurrent = true)
        }
    }
}
