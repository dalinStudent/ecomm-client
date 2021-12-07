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
import com.up.ecomm.MainActivity
import com.up.ecomm.data.PutData


class Register : AppCompatActivity(), View.OnClickListener {
    var textInputEditTextFirstName: TextInputEditText? = null
    var textInputEditTextLastName:TextInputEditText? = null
    private var textInputEditTextEmail:TextInputEditText? = null
    private var textInputEditTextAddress:TextInputEditText? = null
    var textInputEditTextPhone:TextInputEditText? = null
    private var textInputEditTextPassword:TextInputEditText? = null
    var textInputEditTextPasswordConfirm:TextInputEditText? = null
    private var btnSignUp: Button? = null
    var loginText: TextView? = null
    var progressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        textInputEditTextFirstName = findViewById(R.id.first_name)
        textInputEditTextLastName = findViewById(R.id.last_name)
        textInputEditTextEmail = findViewById(R.id.email)
        textInputEditTextAddress = findViewById(R.id.address)
        textInputEditTextPhone = findViewById(R.id.phone)
        textInputEditTextPassword = findViewById(R.id.password)
        textInputEditTextPasswordConfirm = findViewById(R.id.cm_password)
        loginText = findViewById(R.id.login_text)
        btnSignUp = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progress_register)

        loginText!!.setOnClickListener { v: View? ->
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        btnSignUp!!.setOnClickListener {
            val firstName: String = textInputEditTextFirstName?.text?.trim().toString()
            val lastName: String = textInputEditTextLastName?.text?.trim().toString()
            val email: String = textInputEditTextEmail?.text?.trim().toString()
            val address: String = textInputEditTextAddress?.text?.trim().toString()
            val phone: String = textInputEditTextPhone?.text?.trim().toString()
            val password: String = textInputEditTextPassword?.text?.trim().toString()
            if (firstName != "" && lastName != "" && password != "" && email != "" && address != "" && phone != "") {
                progressBar!!.visibility = View.VISIBLE
                val handler = Handler(Looper.getMainLooper())
                handler.post {

                    //Starting Write and Read data with URL
                    //Creating array for parameters
                    val field = arrayOfNulls<String>(6)
                    field[0] = "first_name"
                    field[1] = "last_name"
                    field[2] = "email"
                    field[3] = "address"
                    field[4] = "phone_number"
                    field[5] = "password"
                    //Creating array for data
                    val data = arrayOfNulls<String>(6)
                    data[0] = firstName
                    data[1] = lastName
                    data[2] = email
                    data[3] = address
                    data[4] = phone
                    data[5] = password
                    val putData =
                        PutData("http://192.168.1.10:8000/api/register", "POST", field, data)
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            progressBar!!.visibility = View.GONE
                            val result = putData.result
                            if (result != null) {
                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            Log.i("PutData", result)
                        }
                    }
                }
            } else {
                validate()
            }
        }

    }

    private fun validate() :Boolean {
        if (textInputEditTextFirstName?.text?.trim().toString().isEmpty()) {
            textInputEditTextFirstName?.error = "First Name is required"
            return false
        } else if(textInputEditTextLastName?.text?.trim().toString().isEmpty()) {
            textInputEditTextLastName?.error = "Last Name is required"
            return false
        } else if(textInputEditTextEmail?.text?.trim().toString().isEmpty()) {
            textInputEditTextEmail?.error = "Email address is required"
            return false
        } else if(textInputEditTextAddress?.text?.trim().toString().isEmpty()) {
            textInputEditTextAddress?.error = "Address is required"
            return false
        } else if(textInputEditTextPhone?.text?.trim().toString().isEmpty()){
            textInputEditTextPhone?.error = "Phone is required"
            return false
        } else if(textInputEditTextPassword?.text?.trim().toString().isEmpty()){
            textInputEditTextPassword?.error = "Password is required"
            return false
        } else if (!textInputEditTextPassword?.equals(textInputEditTextPasswordConfirm)!!) {
            textInputEditTextPassword?.error = "Password does not match"
            return false
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