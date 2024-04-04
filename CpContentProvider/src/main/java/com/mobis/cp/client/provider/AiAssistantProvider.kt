package com.mobis.cp.client.provider

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.mobis.cp.client.model.AiAssistantStatus
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

    @SuppressLint("Range")
    fun callAgentStatus(): String {
        var status = ""

        val method = contentResolver.getType(CONTENT_URI)
        Log.d(tag, "callAgentStatus : method:$method")
        method?.let {
            val bundle = contentResolver.call(CONTENT_URI, "getAgentStatus", null, null)
            bundle?.let {
                try {
                    val resultData = it.getString("result")
                    Log.d(tag, "callAgentStatus : resultData:$resultData")
                    if (!resultData.isNullOrEmpty()) {
                        val element = JsonParser.parseString(resultData)
                        val data = Gson().fromJson(element, AiAssistantStatus::class.java)
                        Log.d(tag, "callAgentStatus: $data")
                        status = data.status
                    } else {
                        status = ""
                    }
                } catch (e: Exception) {
                    status = "Error"
                    Log.e(tag, "${e.printStackTrace()}")
                }
            }
        }
        return status
    }

    companion object {
        private const val AUTHORITY = "ai.umos.aiassistant.provider"
        private const val BASE_PATH = "status"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$BASE_PATH")
    }
}