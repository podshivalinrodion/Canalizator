package com.example.canalizator

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected fun setupTopBar(titleText: String) {
        val title = findViewById<TextView?>(R.id.tvTitle)
        val menuLeft = findViewById<ImageView?>(R.id.ivMenuLeft)
        val menuRight = findViewById<ImageView?>(R.id.ivMenuRight)
        val bluetooth = findViewById<ImageView?>(R.id.ivBluetooth)

        title?.text = titleText

        menuLeft?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        bluetooth?.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            startActivity(intent)
        }

        menuRight?.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun showInfoDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_app_info, null)

        view.findViewById<TextView>(R.id.tvAppVersion).text = "Версия приложения: 1.0.0"
        view.findViewById<TextView>(R.id.tvCarInfo).text = "Автомобиль: BMW X5 E70"
        view.findViewById<TextView>(R.id.tvConnectionStatus).text = "Подключение: Bluetooth OBD-II"
        view.findViewById<TextView>(R.id.tvEcuStatus).text = "ECU: подключено"

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Закрыть", null)
            .show()
    }
}