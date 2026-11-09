package com.example.canalizator

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val menuLeft = findViewById<ImageView>(R.id.ivMenuLeft)
        val menuRight = findViewById<ImageView>(R.id.ivMenuRight)
        val bluetooth = findViewById<ImageView>(R.id.ivBluetooth)
        val title = findViewById<TextView>(R.id.tvTitle)

        title.text = "Настройки"

        menuLeft.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        bluetooth.setOnClickListener {
            startActivity(Intent(this, BluetoothActivity::class.java))
        }

        menuRight.setOnClickListener {
            showInfoDialog()
        }

        val clickableIds = listOf(
            R.id.itemCars,
            R.id.itemAdapter,
            R.id.itemProfile,
            R.id.itemInterface,
            R.id.itemUnits,
            R.id.itemDashboard,
            R.id.itemVehicleParams,
            R.id.itemFuel,
            R.id.itemSensors,
            R.id.itemRate,
            R.id.itemPro,
            R.id.itemBackup,
            R.id.itemNotifications,
            R.id.itemTheme,
            R.id.itemLanguage,
            R.id.itemAbout
        )

        clickableIds.forEach { id ->
            findViewById<LinearLayout>(id).setOnClickListener {
                Toast.makeText(this, "Раздел в разработке", Toast.LENGTH_SHORT).show()
            }
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