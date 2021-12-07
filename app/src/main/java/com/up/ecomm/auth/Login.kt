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
import com.up.ecomm.MainActivity




class Login : AppCompatActivity(), View.OnClickListener {
    private var textInputEditTextPassword: TextInputEditText? = null
    private var textInputEditTextEmail: TextInputEditText? = null
    private var btnSignIn: Button? = null
    private var registerText: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textInputEditTextEmail = findViewById(R.id.emailLogin)
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
            val email: String = textInputEditTextEmail?.text?.trim().toString()
            val password: String = textInputEditTextPassword?.text?.trim().toString()
            if (email != "" && password != "") {
                progressBar!!.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post {

                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(2)
                    field[0] = "email"
                    field[1] = "password"
                    //Creating array for data
                    val data = arrayOfNulls<String>(2)
                    data[0] = email
                    data[1] = password
                    val putData =
                        PutData("http://192.168.1.10:8000/api/login", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar!!.visibility = View.GONE
                            val result = putData.result

                            if (statusCode == 201) {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
                                    .show()
                            }
//                            Log.i("PutData", result)
                        }
                    }
                }
            } else {
                validate()
            }
        }
    }

    private fun validate() :Boolean {
        if(textInputEditTextEmail?.text?.trim().toString().isEmpty()) {
            textInputEditTextEmail?.error = "Email is required"
            return false
        } else if(textInputEditTextPassword?.text?.trim().toString().isEmpty()){
            textInputEditTextPassword?.error = "Password is required"
        }
        return true
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_login-> {
                if (validate()) {
                    Toast.makeText(applicationContext, "Done", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}