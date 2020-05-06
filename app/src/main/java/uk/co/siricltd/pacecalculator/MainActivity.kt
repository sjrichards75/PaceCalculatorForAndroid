package uk.co.siricltd.pacecalculator

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.widget.addTextChangedListener
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt

class MainActivity : AppCompatActivity() {
    lateinit var GoalDistance: EditText
    lateinit var MilesRadio: RadioButton
    lateinit var KMRadio: RadioButton
    lateinit var GoalHrs: EditText
    lateinit var GoalMins: EditText
    lateinit var GoalSecs: EditText
    lateinit var TargetPace: TextView

    var TargetPaceStr: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GoalDistance = findViewById(R.id.goal_distance)
        MilesRadio = findViewById(R.id.mile_units)
        KMRadio = findViewById(R.id.km_units)
        GoalHrs = findViewById(R.id.goal_hours)
        GoalMins = findViewById(R.id.goal_minutes)
        GoalSecs = findViewById(R.id.goal_seconds)
        TargetPace = findViewById(R.id.target_pace)
        val CalculateButton: Button = findViewById(R.id.calc_button)
        CalculateButton.setOnClickListener{
            TargetPaceStr = getTargetPaceString()
            TargetPace.text = TargetPaceStr
            val pnl: LinearLayout = findViewById(R.id.calc_pace_panel)
            pnl.visibility = View.VISIBLE
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
        MilesRadio.setOnClickListener {
            KMRadio.isChecked = false
        }
        KMRadio.setOnClickListener {
            MilesRadio.isChecked = false
        }
    }

    fun parseIntOrZero(input: String): Int {
        try {
            return Integer.parseInt(input)
        }
        catch(e: Exception) {
            return 0
        }
    }

    fun parseFloatOrZero(input: String): Float {
        try {
            return parseFloat(input)
        }
        catch(e: Exception) {
            return 0.0f
        }
    }

    fun getTargetPaceString(): String {
        // validate input
        var hh: Int = parseIntOrZero( GoalHrs.text.toString() )
        var mm: Int = parseIntOrZero( GoalMins.text.toString() )
        var ss: Int = parseIntOrZero( GoalSecs.text.toString() )
        val total_secs: Int = ((hh * 60) + mm) * 60 + ss
        val secs_per: Float = when {
            total_secs == 0 -> 0.0f
            parseFloatOrZero(GoalDistance.text.toString()) == 0.0f -> 0.0f
            else -> total_secs / parseFloatOrZero(GoalDistance.text.toString())
        }

        when(secs_per) {
            0.0f -> {
                return "Target pace could not be calculated"
            }
            else -> {
                hh = (secs_per / 60 / 60).toInt()
                mm = ((secs_per - (hh * 60 * 60)) / 60).toInt()
                ss = (secs_per - (hh * 60 * 60) - (mm * 60)).toInt()

                val units: String = when {
                    MilesRadio.isChecked -> "mile"
                    else -> "KM"
                }

                return "Target Pace: " +
                        hh.toString().padStart(2, '0') + ":" +
                        mm.toString().padStart(2, '0') + ":" +
                        ss.toString().padStart(2, '0') +
                        " per ${units}"
            }
        }


    }
}
