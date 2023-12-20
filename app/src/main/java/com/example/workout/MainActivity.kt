package com.example.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.example.workout.databinding.ActivityMainBinding
import android.content.Intent as Intent

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.flstart?.setOnClickListener {
            val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBmi?.setOnClickListener {
            val intent = Intent(this,BmiActivity::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener(){
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}