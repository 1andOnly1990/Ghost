package com.ghost.legion.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ghost.legion.domain.usecase.ProcessFactionTickUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FactionTickWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val processFactionTickUseCase: ProcessFactionTickUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            processFactionTickUseCase()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "faction_tick_work"
    }
}
