package com.example.voicerecorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        output = Environment.getExternalStorageDirectory().absolutePath+"/recording.mp3"
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(output)
        val start = findViewById<Button>(R.id.button_start_recording)
        val stop = findViewById<Button>(R.id.button_stop_recording)
        val pause = findViewById<Button>(R.id.button_pause_recording)

        start.setOnClickListener{
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    var permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    ActivityCompat.requestPermissions(this, permissions,0)
                } else {
                    startRecording()
                }
            }
        stop.setOnClickListener{
            stopRecording()
        }

        pause.setOnClickListener {
            pauseRecording()
        }
        }
    private fun startRecording(){
        try{
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            state = true
            Toast.makeText(this, "recording started", Toast.LENGTH_SHORT).show()
        }catch (e: IllegalStateException){
            e.printStackTrace()
        }catch (e : IOException){
            e.printStackTrace()
        }
    }
    private fun stopRecording(){
        if(state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            state=false
        }else{
            Toast.makeText(this, "you are not recording right now", Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("RestrictedApi", "SetTextI18n")
    private fun pauseRecording(){
        val pause = findViewById<Button>(R.id.button_pause_recording)
        if(state){
            if(!recordingStopped){
                Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show()
                mediaRecorder?.pause()
                recordingStopped= true
                pause.text = "resume"
            }
            else{
                resumeRecording()
            }
        }
    }
    @SuppressLint("RestrictedApi", "SetTextI18n")
    private fun resumeRecording(){
        val pause = findViewById<Button>(R.id.button_pause_recording)
        Toast.makeText(this,"Resume!", Toast.LENGTH_SHORT).show()
        mediaRecorder?.resume()
        pause.text = "Pause"
        recordingStopped = false
    }
    }

