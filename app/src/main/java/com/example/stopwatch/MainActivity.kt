package com.example.stopwatch

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val  binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
   private var isRunning= false
    private var minutes:String?="00.00.00"
    @SuppressLint("SetTextI18n", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val lapsList=ArrayList<String>()
        val arrayAdapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lapsList)
        binding.listview.adapter=arrayAdapter
        binding.lap.setOnClickListener{
            if(isRunning){
                lapsList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        binding.clock.setOnClickListener{
                val dialog=Dialog(this)
                dialog.setContentView(R.layout.dialouge)
                val numberPicker=dialog.findViewById<NumberPicker>(R.id.numberpicker)
            numberPicker.minValue=0
            numberPicker.maxValue=5
            dialog.findViewById<Button>(R.id.set_time).setOnClickListener{
                minutes=numberPicker.value.toString()
                binding.clocktime.text= dialog.findViewById<NumberPicker>(R.id.numberpicker).value.toString()+" mins"
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.run.setOnClickListener {
            if (!isRunning) {
                isRunning = false
                if (!minutes.equals("00.00.00")) {
                    val totalmin = minutes!!.toInt() * 60 * 1000L

                    binding.chronometer.base = SystemClock.elapsedRealtime() + totalmin
                    binding.chronometer.format = "%S %S"
                    binding.chronometer.onChronometerTickListener =
                        Chronometer.OnChronometerTickListener {
                            val elapsedtime =
                                SystemClock.elapsedRealtime() - binding.chronometer.base
                            if (elapsedtime >= totalmin) {
                                binding.chronometer.stop()
                                isRunning = false
                                binding.run.text = "Run"
                            }
                        }

                } else {
                    isRunning = true
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.run.text = "Stop"
                    binding.chronometer.start()

                }
            } else {
                binding.chronometer.stop()
                isRunning = false
                binding.run.text = "Run"
            }
        }
    }
}