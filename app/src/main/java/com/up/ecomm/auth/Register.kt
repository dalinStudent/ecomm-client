package com.up.ecomm.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.up.ecomm.R
import android.widget.ProgressBar

import android.widget.TextView

import com.google.android.material.textfield.TextInputEditText
import android.content.Intent
import android.os.Handler
import android.view.View
import android.widget.Toast

import android.os.Looper
import android.util.Log
import com.up.ecomm.data.PutData
import java.util.regex.Pattern


class Register : AppCompatActivity(), View.OnClickListener {
    private var textInputEditTextFullname: TextInputEditText? = null
    var textInputEditTextPassword:TextInputEditText? = null
    var textInputEditTextUsername:TextInputEditText? = null
    var textInputEditTextEmail:TextInputEditText? = null
    private var btnSignUp: Button? = null
    var loginText: TextView? = null
    var progressBar: ProgressBar? = null
//    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
//        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
//                "\\@" +
//                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
//                "(" +
//                "\\." +
//                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
//                ")+"
//    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textInputEditTextFullname = findViewById(R.id.fullname)
        textInputEditTextUsername = findViewById(R.id.username)
        textInputEditTextPassword = findViewById(R.id.password)
        textInputEditTextEmail = findViewById(R.id.email)
        loginText = findViewById(R.id.login_text)
        btnSignUp = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progress_register)

        loginText!!.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        btnSignUp!!.setOnClickListener {
            val fullname: String = textInputEditTextFullname?.text?.trim().toString()
            val username: String = textInputEditTextUsername?.text?.trim().toString()
            val password: String = textInputEditTextPassword?.text?.trim().toString()
            val email: String = textInputEditTextEmail?.text?.trim().toString()
            if (fullname != "" && username != "" && password != "" && email != "") {
                progressBar!!.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post {

                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(4)
                    field[0] = "fullname"
                    field[1] = "username"
                    field[2] = "password"
                    field[3] = "email"
                    //Creating array for data
                    val data = arrayOfNulls<String>(4)
                    data[0] = fullname
                    data[1] = username
                    data[2] = password
                    data[3] = email
                    val putData =
                        PutData("http://192.168.1.10/server/signup.php", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar!!.visibility = View.GONE
                            val result = putData.result
                            if (result == "Sign Up Success") {
                                val intent =
                                    Intent(applicationContext, Login::class.java)
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
                validate()
            }
        }

    }
//    private fun isValidString(str: String): Boolean{
//        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
//    }

    private fun validate() :Boolean {
        if (textInputEditTextFullname?.text?.trim().toString().isEmpty()) {
            textInputEditTextFullname?.error = "Full Name is required"
            return false
        } else if(textInputEditTextEmail?.text?.trim().toString().isEmpty()) {
            textInputEditTextEmail?.error = "Email address is required"
        } else if(textInputEditTextUsername?.text?.trim().toString().isEmpty()) {
            textInputEditTextUsername?.error = "Username is required"
        } else if(textInputEditTextPassword?.text?.trim().toString().isEmpty()){
            textInputEditTextPassword?.error = "Password is required"
        }

        return true
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_register-> {
                if (validate()) {
                    Toast.makeText(applicationContext, "Done", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}