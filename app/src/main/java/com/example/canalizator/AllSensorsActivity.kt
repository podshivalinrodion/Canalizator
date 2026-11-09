package com.example.canalizator

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.SimpleExpandableListAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AllSensorsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_sensors)

        val menuLeft = findViewById<ImageView>(R.id.ivMenuLeft)
        val menuRight = findViewById<ImageView>(R.id.ivMenuRight)
        val bluetooth = findViewById<ImageView>(R.id.ivBluetooth)
        val title = findViewById<TextView>(R.id.tvTitle)
        val expandableListView = findViewById<ExpandableListView>(R.id.expandableSensors)

        title.text = "Все датчики"

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

        val groups = listOf(
            mapOf("GROUP_NAME" to "Двигатель"),
            mapOf("GROUP_NAME" to "Трансмиссия"),
            mapOf("GROUP_NAME" to "Тормозная система"),
            mapOf("GROUP_NAME" to "Рулевое управление"),
            mapOf("GROUP_NAME" to "Подвеска"),
            mapOf("GROUP_NAME" to "Кузов"),
            mapOf("GROUP_NAME" to "Климат"),
            mapOf("GROUP_NAME" to "Электрика"),
            mapOf("GROUP_NAME" to "Топливная система")
        )

        val children = listOf(
            listOf(
                mapOf("CHILD_NAME" to "Обороты двигателя: 3200 RPM"),
                mapOf("CHILD_NAME" to "Температура охлаждающей жидкости: 86°C"),
                mapOf("CHILD_NAME" to "Температура масла: 91°C"),
                mapOf("CHILD_NAME" to "Положение дросселя: 24%"),
                mapOf("CHILD_NAME" to "Нагрузка двигателя: 41%"),
                mapOf("CHILD_NAME" to "Давление во впуске: 98 kPa")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Передача: 4"),
                mapOf("CHILD_NAME" to "Температура масла АКПП: 74°C"),
                mapOf("CHILD_NAME" to "Давление АКПП: 5.2 bar"),
                mapOf("CHILD_NAME" to "Состояние гидротрансформатора: активно")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Давление в тормозном контуре: 84 bar"),
                mapOf("CHILD_NAME" to "ABS: активно"),
                mapOf("CHILD_NAME" to "Износ колодок передних: 34%"),
                mapOf("CHILD_NAME" to "Износ колодок задних: 41%")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Угол поворота руля: 12°"),
                mapOf("CHILD_NAME" to "Усилитель руля: норма"),
                mapOf("CHILD_NAME" to "Момент на руле: 2.4 Nm")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Высота передней подвески: 142 мм"),
                mapOf("CHILD_NAME" to "Высота задней подвески: 146 мм"),
                mapOf("CHILD_NAME" to "Жёсткость амортизаторов: средняя")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Дверь водителя: закрыта"),
                mapOf("CHILD_NAME" to "Дверь пассажира: закрыта"),
                mapOf("CHILD_NAME" to "Капот: закрыт"),
                mapOf("CHILD_NAME" to "Багажник: закрыт")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Температура в салоне: 22°C"),
                mapOf("CHILD_NAME" to "Температура снаружи: 11°C"),
                mapOf("CHILD_NAME" to "Скорость вентилятора: 3"),
                mapOf("CHILD_NAME" to "Компрессор кондиционера: включён")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Напряжение аккумулятора: 12.4V"),
                mapOf("CHILD_NAME" to "Ток генератора: 48A"),
                mapOf("CHILD_NAME" to "Освещение: включено"),
                mapOf("CHILD_NAME" to "Состояние предохранителей: норма")
            ),
            listOf(
                mapOf("CHILD_NAME" to "Уровень топлива: 58%"),
                mapOf("CHILD_NAME" to "Давление топлива: 3.8 bar"),
                mapOf("CHILD_NAME" to "Температура топлива: 27°C"),
                mapOf("CHILD_NAME" to "Расход топлива: 8.2 л/100 км")
            )
        )

        val adapter = SimpleExpandableListAdapter(
            this,
            groups,
            R.layout.item_sensor_group,
            arrayOf("GROUP_NAME"),
            intArrayOf(R.id.tvGroupName),
            children,
            R.layout.item_sensor_child,
            arrayOf("CHILD_NAME"),
            intArrayOf(R.id.tvChildName)
        )

        expandableListView.setAdapter(adapter)

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