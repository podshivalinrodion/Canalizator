package com.example.canalizator

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvSpeed: TextView
    private lateinit var tvRpm: TextView
    private lateinit var tvAngle: TextView
    private lateinit var tvLat: TextView
    private lateinit var tvLon: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    private val runnable = object : Runnable {
        override fun run() {

            if (!::tvSpeed.isInitialized) return

            try {
                tvSpeed.text = String.format("%.1f км/ч", DataHolder.speed)

                tvRpm.text = String.format("%.0f rpm", DataHolder.rpm)

                tvAngle.text = if (DataHolder.angle <= -200.0) {
                    "--"
                } else {
                    String.format("%.1f", DataHolder.angle)
                }

                tvLat.text = if (DataHolder.lat <= -200.0) {
                    "--"
                } else {
                    String.format("%.2f", DataHolder.lat)
                }

                tvLon.text = if (DataHolder.lon <= -200.0) {
                    "--"
                } else {
                    String.format("%.2f", DataHolder.lon)
                }

            } catch (_: Exception) {}

            handler.postDelayed(this, 300)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val menuLeft = findViewById<ImageView>(R.id.ivMenuLeft)
        val menuRight = findViewById<ImageView>(R.id.ivMenuRight)
        val bluetooth = findViewById<ImageView>(R.id.ivBluetooth)
        val title = findViewById<TextView>(R.id.tvTitle)

        tvSpeed = findViewById(R.id.tvSpeed)
        tvRpm = findViewById(R.id.tvRpm)
        tvAngle = findViewById(R.id.tvAngle)
        tvLat = findViewById(R.id.tvLat)
        tvLon = findViewById(R.id.tvLon)

        title.text = "Панель"

        menuLeft.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        bluetooth.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        menuRight.setOnClickListener {
            showInfoDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
        handler.removeCallbacks(runnable)
    }

    private fun showInfoDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_app_info, null)

        view.findViewById<TextView>(R.id.tvAppVersion).text = "Версия 1.0"
        view.findViewById<TextView>(R.id.tvCarInfo).text = "BMW X5"

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Закрыть", null)
            .show()
    }
}