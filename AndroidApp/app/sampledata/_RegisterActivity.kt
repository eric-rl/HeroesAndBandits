package com.example.kotlinmessenger

import android.app.Notification
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            performRegister()
        }

        already_have_account_text_view.setOnClickListener {
            Log.d("__RegisterActivity", "Try to show login activity")

            // launch the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        StitchCon
    }

    private fun performRegister() {
        val email = email_edittext_register.text.toString()
        val password = password_edtittext_register.text.toString()

        if (email.isEmpty() || password.length < 6) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
        } else StitchCon.registerUser(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Log.d("___RegisterActivity", "ERROR: ${task.exception}")

            }
        }
    }
}
