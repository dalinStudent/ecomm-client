package com.up.ecomm.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.up.ecomm.R
import com.up.ecomm.data.PutData

class Login : AppCompatActivity() {
    private var textInputEditTextPassword: TextInputEditText? = null
    private var textInputEditTextUsername: TextInputEditText? = null
    private var btnSignIn: Button? = null
    private var registerText: TextView? = null
    private var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textInputEditTextUsername = findViewById(R.id.usernameLogin)
        textInputEditTextPassword = findViewById(R.id.passwordLogin)
        registerText = findViewById(R.id.register_text)
        btnSignIn = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progressLogin)

        registerText!!.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, Register::class.java)
            startActivity(intent)
            finish()
        }

        btnSignIn!!.setOnClickListener {
            val username: String = textInputEditTextUsername?.text?.trim().toString()
            val password: String = textInputEditTextPassword?.text?.trim().toString()
            if (username != "" && password != "") {
                progressBar!!.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post {

                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(2)
                    field[0] = "username"
                    field[1] = "password"
                    //Creating array for data
                    val data = arrayOfNulls<String>(2)
                    data[0] = username
                    data[1] = password
                    val putData =
                        PutData("http://192.168.1.10/server/login.php", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar!!.visibility = View.GONE
                            val result = putData.result
                            if (result == "Login Success") {
                                val intent =
                                    Intent(applicationContext, Register::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            if (result != null) {
                                Log.i("PutData", result)
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(applicationContext, "All fields required!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}