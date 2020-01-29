package com.example.heroesandbandits.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.heroesandbandits.R
import com.example.heroesandbandits.Utils.StitchCon
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val password = password_edittext_login.text.toString()
            if (email.isEmpty() || password.length < 6) {
                Toast.makeText(
                    this,
                    "Please enter text in email/pw >= 5", Toast.LENGTH_SHORT
                ).show()
            } else {
                login(email, password)
            }
            d("__LoginActivity", "Attempt login with email/pw: $email / $password")
        }

        back_to_register_textview.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
    }

    fun login(email: String, password: String) {
        StitchCon.login(email, password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    this,
                    "Successfully logged in as user " + it.result.id, Toast.LENGTH_LONG
                ).show()
                d("___", "Successfully logged in as user " + it.result.id)
                StitchCon.initUser()
                startActivity(Intent(this, SearchActivity::class.java))
            } else {
                Log.e("___", "Error logging in with email/password auth:", it.exception)
            }
        }
    }
}