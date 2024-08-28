package com.fevr.personaltracker.workManager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.fevr.personaltracker.roomResources.ActivitiesToDo
import com.fevr.personaltracker.viewModels.HabitTrackerViewModel
import com.fevr.personaltracker.viewModels.WorkTrackerViewmodel
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class DayChangeWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {

        val id = inputData.getInt("id", 0)
        val description = inputData.getString("description") ?: return Result.failure()
        val state = inputData.getBoolean("state", true)

        // Your function to execute when the day changes
        performDayChangeTasks(ActivitiesToDo(id, description, state), applicationContext)
        return Result.success()
    }
}

class ClearWork(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        // Your function to execute when the day changes
        clearWorkSplits(applicationContext)
        return Result.success()
    }
}

fun scheduleClearWorks(context: Context) {
    val now = LocalDateTime.now()
    val midnight = now.withHour(0).withMinute(0).withSecond(0).plusDays(1)
    val initialDelay = Duration.between(now, midnight).seconds

    val dayChangeRequest = OneTimeWorkRequestBuilder<ClearWork>()
        .setInitialDelay(initialDelay, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(dayChangeRequest)
}

fun scheduleDayChangeWorker(context: Context, activity : ActivitiesToDo) {
    val now = LocalDateTime.now()
    val midnight = now.withHour(0).withMinute(0).withSecond(0).plusDays(1)
    val initialDelay = Duration.between(now, midnight).seconds

    val dayChangeRequest = OneTimeWorkRequestBuilder<DayChangeWorker>()
        .setInputData(workDataOf(
            "id" to activity.id,
            "description" to activity.description,
            "state" to activity.state
        ))
        .setInitialDelay(initialDelay, TimeUnit.SECONDS)
        .build()

    WorkManager.getInstance(context).enqueue(dayChangeRequest)
}

fun performDayChangeTasks(activity: ActivitiesToDo, context: Context) {

    val viewmodel = HabitTrackerViewModel()

    val db = viewmodel.getDatabase(context)

    viewmodel.updateActivityStateNight(db, activity)
}

fun clearWorkSplits(context: Context){
    val viewmodel = WorkTrackerViewmodel()
    val db = viewmodel.getDatabase(context)

    viewmodel.clearWorkSplits(db)

    viewmodel.setStateFalse(context)
}