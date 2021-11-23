package com.android.workmanagerpart1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {

    private var btnStart:Button?=null
    private var txtHasil:TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btnStart)
        txtHasil = findViewById(R.id.txtHasil)

        btnStart?.setOnClickListener {
            setOnTimeWorkRequest()
        }

    }

    private fun setOnTimeWorkRequest(){

        val workManager = WorkManager.getInstance(applicationContext)

        val uploadRequest = OneTimeWorkRequest
            .Builder(UploadWorker::class.java)
            .build()

        workManager.enqueue(uploadRequest)

        //observe hasil
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
                txtHasil?.text = it.state.name
            })
    }
}