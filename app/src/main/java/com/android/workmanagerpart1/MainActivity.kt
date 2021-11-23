package com.android.workmanagerpart1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.work.*

class MainActivity : AppCompatActivity() {

    private var btnStart:Button?=null
    private var txtHasil:TextView?=null

    companion object{
        val KEY_COUNT_VALUE = "key_count"
    }

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

        //request data / kirim data ke Work Manager
        val data = Data.Builder()
            .putInt(KEY_COUNT_VALUE,125)
            .build()

        //constraint
        val constraint = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequest
            .Builder(UploadWorker::class.java)
            .setConstraints(constraint)
            .setInputData(data)
            .build()

        workManager.enqueue(uploadRequest)

        //observe hasil
        workManager.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this, Observer {
                txtHasil?.text = it.state.name

                if (it.state.isFinished){
                    val data = it.outputData
                    val message = data.getString(UploadWorker.KEY_WOrKEr)
                    txtHasil?.text = message
                }

            })
    }
}