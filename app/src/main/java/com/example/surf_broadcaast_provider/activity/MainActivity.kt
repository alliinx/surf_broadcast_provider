package com.example.surf_broadcaast_provider.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.surf_broadcaast_provider.R
import com.example.surf_broadcaast_provider.receiver.CustomBroadcastReceiver

private const val SECRET_KEY = "SECRET_KEY"
private const val RECEIVE_MESSAGE = "RECEIVE_MESSAGE"

class MainActivity : AppCompatActivity() {

    private lateinit var secretKey: String
    private lateinit var receiveMessage: String
    private val customBroadcastReceiver = CustomBroadcastReceiver()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val filter = IntentFilter("ru.shalkoff.vsu_lesson2_2024.SURF_ACTION")
        registerReceiver(customBroadcastReceiver, filter, RECEIVER_EXPORTED)

        secretKey = "no key"
        receiveMessage = "no message"

        val providerBtn = findViewById<Button>(R.id.btn_provider)
        providerBtn.setOnClickListener{
            getSecretKey()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val key = savedInstanceState.getString(SECRET_KEY)
        val message = savedInstanceState.getString(RECEIVE_MESSAGE)
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
        key?.let { Log.d("onRestoreInstanceState", it) }
        message?.let { Log.d("onRestoreInstanceState", it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            SECRET_KEY,
            secretKey
        )
        outState.putString(
            RECEIVE_MESSAGE,
            receiveMessage
        )
        Log.d("onSaveInstanceState", secretKey)
        Log.d("onSaveInstanceState", receiveMessage)
    }


    @SuppressLint("Range")
    private fun getSecretKey(){
        val resolver = contentResolver

        val uri = Uri.parse("content://dev.surf.android.provider/text")

        val cursor = resolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val text = it.getString(it.getColumnIndex("text"))
                // Используем полученные данные
                secretKey = text
                Toast.makeText(this,text, Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(this,"no secret key", Toast.LENGTH_SHORT).show()

        }
    }
}