package com.example.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.workout.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private var binding: ActivityHistoryBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarHistory)
            if (supportActionBar!= null){
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.title = "HISTORY"
            }
            binding?.toolbarHistory?.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
    }
}