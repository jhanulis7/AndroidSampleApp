package ai.umos.ambientai.feature.navi42.provider

import ai.umos.ambientai.feature.navi42.model.Navi42SocData
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class Navi42Provider @Inject constructor(
    @ApplicationContext context: Context
) {
    private val tag = "[AmbientAi] Navi42Provider"
    private val contentResolver: ContentResolver = context.contentResolver

    fun getSettingsSocData(): Navi42SocData {
        Log.d(tag, "getSettingsSocData")
        var cursor: Cursor? = null
        var navi42SocData = Navi42SocData()

        try {
            cursor = contentResolver.query(
                CONTENT_URI,
                null,
                null, // where 절
                null,
                null // order by
            )

            cursor?.let {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val batterySizeIndex = cursor.getColumnIndex("batterySize")
                    val batteryLevelIndex = cursor.getColumnIndex("batteryLevel")
                    val motorEfficiencyIndex = cursor.getColumnIndex("motorEfficiency")
                    val drivingRangeIndex = cursor.getColumnIndex("drivingRange")

                    val batterySize = cursor.getDouble(batterySizeIndex)
                    val batteryLevel = cursor.getDouble(batteryLevelIndex)
                    val motorEfficiency = cursor.getDouble(motorEfficiencyIndex)
                    val drivingRange = cursor.getDouble(drivingRangeIndex)
                    Log.v(tag, "[getSettingsSocData] batterySize[$batterySize] batteryLevel[$batteryLevel] motorEfficiency[$motorEfficiency] drivingRange[$drivingRange]")
                    navi42SocData = Navi42SocData(batterySize, batteryLevel, motorEfficiency, drivingRange)
                    cursor.moveToNext()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "fetchRadioPreset e:$e")
        }
        cursor?.close()
        return navi42SocData
    }

    companion object {
        private const val TABLE_NAME = "Battery"
        private const val AUTHORITY = "ai.umos.maps.android.navigation.app"
        private const val URI_STRING = "content://$AUTHORITY/$TABLE_NAME"
        val CONTENT_URI: Uri = Uri.parse(URI_STRING)
    }
}