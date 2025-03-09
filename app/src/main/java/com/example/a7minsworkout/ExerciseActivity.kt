package com.example.a7minsworkout

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.a7minsworkout.databinding.ActivityExerciseBinding
import com.example.a7minsworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    private var player: MediaPlayer? = null
    private var exerciseTimerDuration: Long = 30

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    //
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)

        onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showCustomExitDialog()
            }
        })


        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarExercise?.setNavigationOnClickListener {
            showCustomExitDialog()
        }

        tts = TextToSpeech(this, this)
        exerciseList = Constants.defaultExerciseList()
        setUpRestView()
        setupExerciseStatusRecyclerView()
    }

    private fun showCustomExitDialog(){
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_back_confirmation, null)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.button_yes).setOnClickListener {
            finish()
        }
        dialogView.findViewById<Button>(R.id.button_no).setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }





    private fun setupExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager =
                //Sıraları yanyana koyar.
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }


    private fun setUpRestView() {

        try {
            val soundURI = Uri.parse(
                "android.resource://com.example.a7minsworkout/"
                        + R.raw.press_start
            )
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.upcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        binding?.tvUpcomingExerciseName?.text =
            exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    private fun setUpExerciseView() {
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE
        binding?.upcomingLabel?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())


        Glide.with(this)
            .asGif()
            .load(exerciseList!![currentExercisePosition].getImage())
            .into(binding?.ivImage!!)

        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()


        setExerciseProgressBar()

    }

    private fun setRestProgressBar() {
        binding?.progressBar?.progress = restProgress


        restTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text =
                    (10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setUpExerciseView()
            }
        }.start()
    }

    private fun setExerciseProgressBar() {
        binding?.progressBarEx?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(
            exerciseTimerDuration * 100,
            100
        ) {
            override fun onTick(milisUntilFinished: Long) {
                exerciseProgress++
                binding?.progressBarEx?.progress = exerciseTimerDuration.toInt() - exerciseProgress
                binding?.tvTimerEx?.text =
                    (exerciseTimerDuration.toInt() - exerciseProgress).toString()
            }

            override fun onFinish() {

                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setUpRestView() // Sonraki egzersize geç
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)

                }
            }
        }.start()
    }


    override fun onDestroy() {
        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }
        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }


        super.onDestroy()
        binding = null
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun speakOut (text: String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}


