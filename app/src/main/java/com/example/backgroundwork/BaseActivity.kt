package com.example.backgroundwork

import android.app.Activity
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.backgroundwork.service.bind.binding.BindingActivity
import com.example.backgroundwork.service.bind.messenger.ActivityMessenger
import com.example.backgroundwork.service.foreground.MyCameraService
import com.example.backgroundwork.service.job.MyJobService
import com.example.backgroundwork.service.worker.UserDataUploadWorker
import java.util.concurrent.TimeUnit


class BaseActivity: Activity() {
    lateinit var button: Button
    lateinit var scheduleJobBtn: Button
    lateinit var oneTimeWorker:Button
    lateinit var periodicWorker:Button
    lateinit var constraintsBtn : Button
    lateinit var extendBind : Button
    lateinit var messengerBind : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        button = findViewById(R.id.foreground_btn)
        scheduleJobBtn = findViewById(R.id.scheduleJob_btn)
        oneTimeWorker = findViewById(R.id.oneTime_btn)
        periodicWorker = findViewById(R.id.periodic_btn)
        constraintsBtn = findViewById(R.id.constraints_btn)
        extendBind = findViewById(R.id.extendBindBtn)
        messengerBind = findViewById(R.id.messengerBindBtn)

        button.setOnClickListener {
            val myService = Intent(this, MyCameraService::class.java)
            myService.putExtra("key" , "")
                startService(myService)

        }
        scheduleJobBtn.setOnClickListener {
            scheduleJob()
        }
        oneTimeWorker.setOnClickListener {
            val uploadWorkRequest = OneTimeWorkRequestBuilder<UserDataUploadWorker>().build()
            WorkManager.getInstance(this).enqueue(uploadWorkRequest)
        }
        periodicWorker.setOnClickListener {
            val periodicWorkRequest = PeriodicWorkRequestBuilder<UserDataUploadWorker>(24, TimeUnit.HOURS).build()
            WorkManager.getInstance(this).enqueue(periodicWorkRequest)
            WorkManager.getInstance(this).cancelWorkById(periodicWorkRequest.id)
        }
        constraintsBtn.setOnClickListener {
            val constraints = Constraints.Builder()
                //.setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                //.setRequiresStorageNotLow(true)
                //.setRequiresDeviceIdle(true)
                .build()
            val oneTimeWorkRequest = OneTimeWorkRequestBuilder<UserDataUploadWorker>()
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .build()
            WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)

        }

        extendBind.setOnClickListener {
            startActivity(Intent(this , BindingActivity::class.java))
        }
        messengerBind.setOnClickListener {
            startActivity(Intent(this ,ActivityMessenger::class.java))
        }
    }
    private fun scheduleJob() {
        val jobScheduler = getSystemService(
            JOB_SCHEDULER_SERVICE
        ) as JobScheduler

        val name = ComponentName(applicationContext, MyJobService::class.java)

        val result = jobScheduler.schedule(getJobInfo(123, 1, name)!!)

        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Scheduled job successfully!")
        }
    }

    private fun getJobInfo(id: Int, hour: Long, name: ComponentName): JobInfo? {
        val interval: Long = TimeUnit.HOURS.toMillis(hour) // run every hour
        val isPersistent = true // persist through boot
        val networkType = JobInfo.NETWORK_TYPE_ANY // Requires some sort of connectivity
        val jobInfo: JobInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            JobInfo.Builder(id, name)
                .setMinimumLatency(interval)
                .setRequiredNetworkType(networkType)
                .setPersisted(isPersistent)
                .build()
        } else {
            JobInfo.Builder(id, name)
                .setPeriodic(interval)
                .setRequiredNetworkType(networkType)
                .setPersisted(isPersistent)
                .build()
        }
        return jobInfo
    }
}