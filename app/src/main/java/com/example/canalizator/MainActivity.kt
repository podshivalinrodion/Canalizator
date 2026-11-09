package com.example.canalizator

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private var bluetoothGatt: BluetoothGatt? = null
    private val jsonBuffer = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // ДОБАВЛЕНО: Обработка отступов для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                systemBars.top + 14,  // отступ сверху +14px
                view.paddingRight,
                systemBars.bottom + 14 // отступ снизу
            )
            insets
        }

        val bluetooth = findViewById<ImageView>(R.id.ivBluetooth)
        val title = findViewById<TextView>(R.id.tvTitle)

        val btnDashboard = findViewById<LinearLayout>(R.id.btnDashboard)
        val btnLiveData = findViewById<LinearLayout>(R.id.btnLiveData)
        val btnAllSensor = findViewById<LinearLayout>(R.id.btnAllSensor)
        val btnGraphs = findViewById<LinearLayout>(R.id.btnGraphs)
        val btnDiagnose = findViewById<LinearLayout>(R.id.btnDiagnose)
        val btnSettings = findViewById<LinearLayout>(R.id.btnSettings)

        title.text = "CAN Анализатор"

        requestBtPermissions()

        bluetooth.setOnClickListener {
            connectBle()
        }

        // меню
        btnDashboard.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        btnLiveData.setOnClickListener {
            startActivity(Intent(this, LiveDataActivity::class.java))
        }

        btnAllSensor.setOnClickListener {
            startActivity(Intent(this, AllSensorsActivity::class.java))
        }

        btnGraphs.setOnClickListener {
            startActivity(Intent(this, GraphsActivity::class.java))
        }

        btnDiagnose.setOnClickListener {
            startActivity(Intent(this, DiagnoseActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun requestBtPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissions = arrayOf(
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.BLUETOOTH_SCAN
            )

            val notGranted = permissions.filter {
                checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
            }

            if (notGranted.isNotEmpty()) {
                requestPermissions(notGranted.toTypedArray(), 1)
            }
        }
    }

    @SuppressLint("MissingPermission", "Deprecated")
    private fun connectBle() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Нет разрешения Bluetooth", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val device = BluetoothAdapter.getDefaultAdapter()
            .getRemoteDevice("6C:EB:BA:8D:80:F0")

        bluetoothGatt = device.connectGatt(this, false, gattCallback)

        Toast.makeText(this, "Подключаемся...", Toast.LENGTH_SHORT).show()
    }

    private val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "✅ Подключено", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                }
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {

            for (service in gatt.services) {
                for (char in service.characteristics) {

                    if (char.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {

                        gatt.setCharacteristicNotification(char, true)

                        val descriptor = char.getDescriptor(
                            UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
                        )

                        descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        descriptor?.let { gatt.writeDescriptor(it) }
                    }
                }
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {

            val chunk = characteristic.value?.let { String(it) } ?: return
            android.util.Log.d("BLE_RAW", chunk)

            jsonBuffer.append(chunk)

            while (true) {

                val start = jsonBuffer.indexOf("{")
                val end = jsonBuffer.indexOf("}", start)

                if (start != -1 && end != -1 && end > start) {

                    val jsonStr = jsonBuffer.substring(start, end + 1)
                    jsonBuffer.delete(0, end + 1)

                    parseJson(jsonStr)

                } else {
                    break
                }
            }
        }
    }

    private fun parseJson(text: String) {
        try {
            val json = JSONObject(text)

            val speed = json.optDouble("speed", DataHolder.speed)
            val rpm = json.optDouble("rpm", DataHolder.rpm)
            val angle = json.optDouble("angle", DataHolder.angle)
            val lat = json.optDouble("lat", DataHolder.lat)
            val lon = json.optDouble("lon", DataHolder.lon)

            // фильтр мусора
            if (speed in 0.0..400.0) {
                DataHolder.speed = speed
            }

            if (rpm in 0.0..10000.0) {
                DataHolder.rpm = rpm
            }

            if (angle != -210.0) {
                DataHolder.angle = angle
            }

            if (lat != -210.0) {
                DataHolder.lat = lat
            }

            if (lon != -210.0) {
                DataHolder.lon = lon
            }

        } catch (e: Exception) {
            android.util.Log.e("JSON", "parse error: $text")
        }
    }
}