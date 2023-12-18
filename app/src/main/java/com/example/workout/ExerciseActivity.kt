package com.example.workout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workout.databinding.ActivityExerciseBinding
import com.example.workout.databinding.DialogueCustomBackConfirmationBinding
import java.lang.Exception
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding : ActivityExerciseBinding? = null

    private var restTimer : CountDownTimer?=null
    private var restProgress = 0

    private var exerciseTimer : CountDownTimer?=null
    private var exerciseProgress = 0

    private var restTimeDuration: Long = 1
    private var exerciseTimeDuration: Long = 1

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdaptor : ExerciseStatusAdaptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if (supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
            exerciseList = Constants.defaultExerciseList()

            tts = TextToSpeech(this, this)



        setupRestView()
        setupRecyclerView()
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogueCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
       //super.onBackPressed()
        customDialogForBackButton()
    }

    private fun setupRestView(){

        try {
            val soundUri = Uri.parse(
                "android.resource://com.example.workout/"
                    + R.raw.press_start)
            player = MediaPlayer.create(applicationContext,soundUri)
            player?.isLooping = false
            player?.start()
        }
        catch (e:Exception){
            e.printStackTrace()
        }


        binding?.RestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.tvUp?.visibility = View.VISIBLE
        binding?.tvUpTitle?.visibility = View.VISIBLE

        binding?.tvUpTitle?.text = exerciseList!![currentExercisePosition+1].getName()

        if (restTimer != null ){
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    @SuppressLint("SetTextI18n")
    private fun setExerciseView(){
        binding?.RestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.tvUp?.visibility = View.INVISIBLE
        binding?.tvUpTitle?.visibility = View.INVISIBLE


        if (exerciseTimer != null ){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
            speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImg())
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()
        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimeDuration*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                    binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10 - restProgress).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdaptor!!.notifyDataSetChanged()
                 setExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.pbExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimeDuration*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding?.pbExercise?.progress = 30 -  exerciseProgress
                binding?.tvTimerExercise?.text = (30 - exerciseProgress).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {

                if (currentExercisePosition < exerciseList!!.size -1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdaptor!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (restTimer != null ){
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null ){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        if (player!= null)
        {
            player!!.stop()
        }
        binding = null
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.UK)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","LANGUAGE SPECIFIED IS NOT SUPPORTED")
            }else{
                Log.i("TTS","INITIALIZATION FAILED!")
            }
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null)
    }

    private fun setupRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdaptor = ExerciseStatusAdaptor(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdaptor
    }
}