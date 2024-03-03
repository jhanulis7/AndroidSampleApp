package com.mobis.cp.client.provider

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AiAssistantProvider @Inject constructor(
    @ApplicationContext context: Context
) {
    private val tag = "[AmbientAi] AiAssistantProvider"
    private val contentResolver: ContentResolver = context.contentResolver

    @SuppressLint("Range")
    fun getAgentStatus(): String {
        Log.d(tag, "getAgentStatus")
        var cursor: Cursor? = null
        var status = ""

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
                    status = it.getString(it.getColumnIndex("status"))
                    cursor.moveToNext()
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "getAgentStatus e:$e")
        }
        cursor?.close()
        return status
    }

    companion object {
        private const val AUTHORITY = "ai.umos.aiassistant.provider"
        private const val BASE_PATH = "status"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$BASE_PATH")
    }
}