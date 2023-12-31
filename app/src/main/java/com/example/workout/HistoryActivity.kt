package com.example.workout

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

        val dao = (application as WorkOutApp).db.historyDao()
        getAllDates(dao)
    }

    private fun getAllDates(historyDao: HistoryDao){
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect{
                    allCompletedDateList ->
                    if (allCompletedDateList.isNotEmpty()){
                        binding?.tvHistory?.visibility = View.VISIBLE
                        binding?.rvHistory?.visibility = View.VISIBLE
                        binding?.tvNoDataAvailable?.visibility = View.INVISIBLE

                        binding?.rvHistory?.layoutManager =  LinearLayoutManager(this@HistoryActivity)

                        val dates = ArrayList<String>()
                        for (date in allCompletedDateList){
                            dates.add(date.date)
                        }
                        val historyAdaptor = HistoryAdaptor(dates)

                        binding?.rvHistory?.adapter = historyAdaptor

                    }
                    else{
                        binding?.tvHistory?.visibility = View.GONE
                        binding?.rvHistory?.visibility = View.GONE
                        binding?.tvNoDataAvailable?.visibility = View.VISIBLE
                    }
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}