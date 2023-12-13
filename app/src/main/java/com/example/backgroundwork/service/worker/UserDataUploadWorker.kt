package com.example.backgroundwork.service.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UserDataUploadWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        uploadUserData()
        Log.e("test" , "test")
        return Result.success()
    }

    private fun uploadUserData() {
        // do upload work here
    }
}