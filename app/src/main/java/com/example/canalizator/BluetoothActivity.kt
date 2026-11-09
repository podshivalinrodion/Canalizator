package com.example.canalizator

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BluetoothActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bluetooth)

        val menuLeft = findViewById<ImageView>(R.id.ivMenuLeft)
        val menuRight = findViewById<ImageView>(R.id.ivMenuRight)
        val bluetooth = findViewById<ImageView>(R.id.ivBluetooth)
        val title = findViewById<TextView>(R.id.tvTitle)

        title.text = "Bluetooth"

        menuLeft.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        bluetooth.setOnClickListener {
        }

        menuRight.setOnClickListener {
            showInfoDialog()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showInfoDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_app_info, null)

        view.findViewById<TextView>(R.id.tvAppVersion).text =
            "Версия приложения: 1.0.0"

        view.findViewById<TextView>(R.id.tvCarInfo).text =
            "Автомобиль: BMW X5"

        view.findViewById<TextView>(R.id.tvConnectionStatus).text =
            "Подключение: Bluetooth OBD-II"

        view.findViewById<TextView>(R.id.tvEcuStatus).text =
            "ECU: подключено"

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Закрыть", null)
            .show()
    }
}