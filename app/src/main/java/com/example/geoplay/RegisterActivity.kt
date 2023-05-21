package com.example.geoplay

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geoplay.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColors()

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val pass = binding.pwdInput.text.toString()
            val confirmPass = binding.pwdConfirmInput.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Lozinke se ne podudaraju!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Popunite sva polja i pokušajte ponovo.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun setColors() {
        val hintTextColor = ColorStateList.valueOf(Color.WHITE)

        val emailLayout: TextInputLayout = findViewById(R.id.emailLayout)
        emailLayout.defaultHintTextColor = hintTextColor

        val pwdLayout: TextInputLayout = findViewById(R.id.passwordLayout)
        pwdLayout.defaultHintTextColor = hintTextColor

        val pwdConfirmLayout: TextInputLayout = findViewById(R.id.confirmPasswordLayout)
        pwdConfirmLayout.defaultHintTextColor = hintTextColor
    }
}