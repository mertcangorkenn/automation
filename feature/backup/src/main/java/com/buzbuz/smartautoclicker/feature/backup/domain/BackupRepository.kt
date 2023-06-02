 
package com.buzbuz.smartautoclicker.feature.backup.domain

import android.content.Context
import android.graphics.Point
import android.net.Uri

import com.buzbuz.smartautoclicker.core.database.ClickDatabase
import com.buzbuz.smartautoclicker.core.domain.Repository
import com.buzbuz.smartautoclicker.feature.backup.data.BackupEngine
import com.buzbuz.smartautoclicker.feature.backup.data.BackupProgress

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

internal class BackupRepository private constructor(context: Context) {

    companion object {

        /** Singleton preventing multiple instances of the BackupRepository at the same time. */
        @Volatile
        private var INSTANCE: BackupRepository? = null

        /**
         * Get the BackupRepository singleton, or instantiates it if it wasn't yet.
         * @param context the Android context.
         * @return the BackupRepository singleton.
         */
        fun getInstance(context: Context): BackupRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = BackupRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private val database: ClickDatabase = ClickDatabase.getDatabase(context)

    private val localDataRepository: Repository = Repository.getRepository(context)

    private val backupEngine: BackupEngine = BackupEngine(
        appDataDir = context.filesDir,
        contentResolver = context.contentResolver,
    )

    /**
     * Create a backup of the provided scenario into the provided file.
     *
     * @param zipFileUri the uri of the file to write the backup into. Must be retrieved using the DocumentProvider.
     * @param scenarios the scenarios to backup.
     * @param screenSize the size of this device screen.
     *
     * @return a flow on the backup creation progress.
     */
    fun createScenarioBackup(zipFileUri: Uri, scenarios: List<Long>, screenSize: Point) = channelFlow  {
        launch {
            backupEngine.createBackup(
                zipFileUri,
                scenarios.map {
                    database.scenarioDao().getCompleteScenario(it)
                },
                screenSize,
                BackupProgress(
                    onError = { send(Backup.Error) },
                    onProgressChanged = { current, max -> send(Backup.Loading(current, max)) },
                    onCompleted = { success, failureCount, compatWarning ->
                        send(Backup.Completed(success.size, failureCount, compatWarning))
                    }
                )
            )
        }
    }

    /**
     * Restore a backup of scenarios from the provided file.
     *
     * @param zipFileUri the uri of the file to read the backup from. Must be retrieved using the DocumentProvider.
     * @param screenSize the size of this device screen.
     *
     * @return a flow on the backup import progress.
     */
    fun restoreScenarioBackup(zipFileUri: Uri, screenSize: Point) = channelFlow {
        launch {
            backupEngine.loadBackup(
                zipFileUri,
                screenSize,
                BackupProgress(
                    onError = { send(Backup.Error) },
                    onProgressChanged = { current, max -> send(Backup.Loading(current, max)) },
                    onVerification = { send(Backup.Verification) },
                    onCompleted = { success, failureCount, compatWarning ->

                        var totalFailures = failureCount
                        val actualSuccess = success.toMutableList()
                        success.forEach { completeScenario ->
                            if (!localDataRepository.addScenarioCopy(completeScenario)) {
                                actualSuccess.remove(completeScenario)
                                totalFailures++
                            }
                        }

                        send(Backup.Completed(actualSuccess.size, totalFailures, compatWarning))
                    }
                )
            )
        }
    }
}