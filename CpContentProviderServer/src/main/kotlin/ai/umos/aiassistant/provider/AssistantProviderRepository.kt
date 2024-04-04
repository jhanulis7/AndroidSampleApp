package ai.umos.aiassistant.provider

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssistantProviderRepository
    @Inject
    constructor(
        @ApplicationContext private val context: Context
    ) {
        private val _uri = AiAssistantProvider.CONTENT_URI

        fun insert(status: String) {
            Log.d("ContentProvider", "insert: $status")
            val insertValue =
                ContentValues().apply {
                    put("status", status)
                }

            try {
                context.contentResolver.insert(_uri, insertValue)
            } catch (e: Exception) {
                Log.e("ContentProvider", "error insert ${e.message}}")
            }
        }

        @SuppressLint("Range", "Recycle")
        fun update(status: String) {
            val updateValues =
                ContentValues().apply {
                    put("status", status)
                }
            try {
                context.contentResolver.update(_uri, updateValues, null, null)
            } catch (e: Exception) {
                Log.e("ContentProvider", "error update ${e.message}}")
            }
            Log.d("ContentProvider", "update: $status")
        }

        @SuppressLint("Range", "Recycle")
        fun query(): Int {
            var count = 0
            try {
                val columns = arrayOf("status")
                val cursor = context.contentResolver.query(_uri, columns, null, null, null)
                Log.d("ContentProvider", "queryData 결과 ${cursor?.count}")
                cursor?.let { cursor ->
                    while (cursor.moveToNext()) {
                        val status = cursor.getString(cursor.getColumnIndex(columns[0]))
                        Log.d("ContentProvider", "#$count -> $status")
                        count++
                    }
                }
            } catch (e: Exception) {
                Log.e("ContentProvider", "error query ${e.message}}")
            }

            return count
        }

        fun deleteAll() {
            var count = 0
            try {
                count = context.contentResolver.delete(_uri, null, null)
            } catch (e: Exception) {
                Log.e("ContentProvider", "error deleteAll ${e.message}}")
            }
            Log.d("ContentProvider", "deleteAll 결과: $count")
        }

        fun updateAgentStatus(agentStatus: String) {
            val bundle =
                Bundle().apply {
                    putString("uri", _uri.toString())
                    putString("agentStatus", agentStatus)
                }
            context.contentResolver.call(_uri, "updateAgentStatus", null, bundle)
        }

        fun getAgentStatus(): String {
            val data = context.contentResolver.call(_uri, "getAgentStatus", null, null)
            val result = data?.getString("result")
            Log.d("ContentProvider", "getAgentStatus 결과: $result")
            return result ?: ""
        }
    }
