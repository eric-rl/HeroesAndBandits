package com.example.heroesandbandits.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_account_text_view.setOnClickListener {
            // launch the login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edtittext_register.text.toString()

        if (email.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
        } else StitchCon.registerUser(email, password)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Log.d("___RegisterActivity", "ERROR: ${task.exception}")
            }
        }
    }
}
