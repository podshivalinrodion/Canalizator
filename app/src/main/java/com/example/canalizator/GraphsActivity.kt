package com.example.canalizator

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.sin
import kotlin.random.Random

class GraphsActivity : AppCompatActivity() {

    private lateinit var chartO2S1: LineChart
    private lateinit var chartO2S2: LineChart
    private lateinit var chartTrim: LineChart

    private lateinit var tvValueO2: TextView
    private lateinit var tvValueO22: TextView
    private lateinit var tvValueTrim: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var timeIndex = 0f

    private val updateRunnable = object : Runnable {
        override fun run() {
            updateCharts()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graphs)

        val menuLeft = findViewById<ImageView>(R.id.ivMenuLeft)
        val menuRight = findViewById<ImageView>(R.id.ivMenuRight)
        val bluetooth = findViewById<ImageView>(R.id.ivBluetooth)
        val title = findViewById<TextView>(R.id.tvTitle)

        chartO2S1 = findViewById(R.id.chartO2S1)
        chartO2S2 = findViewById(R.id.chartO2S2)
        chartTrim = findViewById(R.id.chartTrim)

        tvValueO2 = findViewById(R.id.tvValueO2)
        tvValueO22 = findViewById(R.id.tvValueO22)
        tvValueTrim = findViewById(R.id.tvValueTrim)

        title.text = "Графики"

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

        setupChart(chartO2S1)
        setupChart(chartO2S2)
        setupChart(chartTrim)

        setupDataSet(chartO2S1, "O2S1")
        setupDataSet(chartO2S2, "O2S2")
        setupDataSet(chartTrim, "TRIM")
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }

    private fun setupChart(chart: LineChart) {
        chart.setBackgroundColor(Color.parseColor("#2B2B2B"))
        chart.setDrawGridBackground(false)
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)
        chart.setScaleEnabled(true)
        chart.legend.isEnabled = false

        val description = Description()
        description.text = ""
        chart.description = description

        chart.axisRight.isEnabled = false

        chart.axisLeft.textColor = Color.parseColor("#CFCFCF")
        chart.axisLeft.gridColor = Color.parseColor("#4A4A4A")
        chart.axisLeft.axisLineColor = Color.parseColor("#6A6A6A")

        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.textColor = Color.parseColor("#CFCFCF")
        chart.xAxis.gridColor = Color.parseColor("#4A4A4A")
        chart.xAxis.axisLineColor = Color.parseColor("#6A6A6A")

        chart.setNoDataText("Нет данных")
    }

    private fun setupDataSet(chart: LineChart, label: String) {
        val entries = ArrayList<Entry>()
        val dataSet = LineDataSet(entries, label)

        dataSet.color = Color.WHITE
        dataSet.lineWidth = 2f
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.setDrawFilled(false)

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }

    private fun updateCharts() {
        timeIndex += 1f

        val value1 = (0.4f + 0.3f * sin(timeIndex) + Random.nextFloat() * 0.12f).coerceIn(0.1f, 0.8f)
        val value2 = (0.77f + Random.nextFloat() * 0.02f - 0.01f).coerceIn(0.72f, 0.82f)
        val value3 = (1.2f + 1.1f * sin(timeIndex * 0.7f) + Random.nextFloat() * 0.8f).coerceIn(-0.3f, 3.2f)

        addEntry(chartO2S1, timeIndex, value1)
        addEntry(chartO2S2, timeIndex, value2)
        addEntry(chartTrim, timeIndex, value3)

        tvValueO2.text = String.format("%.2f V", value1)
        tvValueO22.text = String.format("%.2f V", value2)
        tvValueTrim.text = String.format("%.2f %%", value3)
    }

    private fun addEntry(chart: LineChart, x: Float, y: Float) {
        val data = chart.data ?: return
        val dataSet = data.getDataSetByIndex(0) ?: return

        data.addEntry(Entry(x, y), 0)

        if (dataSet.entryCount > 20) {
            dataSet.removeFirst()
            for (i in 0 until dataSet.entryCount) {
                dataSet.getEntryForIndex(i).x = i.toFloat()
            }
        }

        data.notifyDataChanged()
        chart.notifyDataSetChanged()
        chart.setVisibleXRangeMaximum(20f)
        chart.moveViewToX(data.entryCount.toFloat())
        chart.invalidate()
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