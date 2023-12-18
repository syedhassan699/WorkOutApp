package com.example.workout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.workout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BmiActivity : AppCompatActivity() {
    private var binding : ActivityBmiBinding? = null

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
        private const val US_UNITS_VIEW = "US_UNITS_VIEW"
    }

    private var currentVisibleView: String =  METRIC_UNITS_VIEW



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        supportActionBar?.title = "BMI CALCULATOR"
        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
          onBackPressedDispatcher.onBackPressed()
        }

        displayMetricUnit()

        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId:Int ->
            if (checkedId == R.id.rbMetricUnits)
            {
                displayMetricUnit()
            }else{
                displayUsUnit()
            }

        }

        binding?.btnCalculateUnits?.setOnClickListener {
            if (validateMetricUnits())
            {
                val height: Float = binding?.etHeight?.text.toString().toFloat() / 100
                val weight: Float = binding?.etWeight?.text.toString().toFloat()
                val bmi = weight / (height * height)

                binding?.tvBmiValue?.text = bmi.toString()
                displayBMIResult(bmi)

            }
            else{
                    Toast.makeText(this, "PLEASE ENTER VALID DETAILS!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun displayMetricUnit(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilHeight?.visibility = View.VISIBLE
        binding?.tilWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.INVISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.INVISIBLE
        binding?.tilUsWeight?.visibility = View.INVISIBLE

        binding?.etWeight?.text!!.clear()
        binding?.etHeight?.text!!.clear()
        binding?.resultBmi?.visibility = View.GONE
    }
    private fun displayUsUnit(){
        currentVisibleView = US_UNITS_VIEW
        binding?.tilHeight?.visibility = View.INVISIBLE
        binding?.tilWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE
        binding?.tilUsWeight?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()

        binding?.resultBmi?.visibility = View.GONE
    }
    private fun validateMetricUnits():Boolean{
        var isValid = true

        if (binding?.etHeight?.text.toString().isEmpty()){
            isValid=false
        }

        if (binding?.etWeight?.text.toString().isEmpty()){
            isValid= false
        }
        return isValid
    }
    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        }
        else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }
        else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        }
        else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        }
        else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        }
        else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }
        else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        //Use to set the result layout visible
        binding?.resultBmi?.visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.tvBmiValue?.text = bmiValue
        binding?.tvBmiType?.text = bmiLabel
        binding?.tvBmiDescription?.text = bmiDescription
    }
}