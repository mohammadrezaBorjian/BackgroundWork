package com.example.backgroundwork.service.job

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.os.HandlerThread
import android.util.Log

@SuppressLint("SpecifyJobSchedulerIdRange")
class MyJobService : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        val handlerThread = HandlerThread("SomeOtherThread")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            Log.e(TAG, "Running!!!!!!!!!!!!!")
            jobFinished(params, true)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        Log.d(TAG, "onStopJob() was called")
        return true
    }

    companion object {
        private val TAG = MyJobService::class.java.simpleName
    }
}