package ai.umos.aiassistant.provider

import ai.umos.aiassistant.provider.AiAssistantContract.TABLE_NAME
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.sql.SQLException
import javax.inject.Singleton

@Singleton
class AiAssistantProvider : ContentProvider() {
    private lateinit var database: SQLiteDatabase
    private val appContext by lazy { context?.applicationContext ?: throw IllegalStateException() }
    private var agentStatus = ""

    override fun onCreate(): Boolean {
        Log.d("ContentProvider", "onCreate()")
        database = DatabaseHelper.getInstance(appContext).writableDatabase
        return true
    }

    override fun query(
        uri: Uri,
        projcetion: Array<out String>?,
        selection: String?,
        selctionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d("ContentProvider", "query() ${uriMatcher.match(uri)}")
        val cursor =
            when (uriMatcher.match(uri)) {
                STATUS -> {
                    database.query(TABLE_NAME, projcetion, selection, selctionArgs, null, null, sortOrder)
                }
                else -> throw IllegalArgumentException("Unknown URI : $uri")
            }
        cursor.setNotificationUri(appContext.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String {
        Log.d("ContentProvider", "getType()")

        when (uriMatcher.match(uri)) {
            STATUS -> return "$AUTHORITY.$TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI : $uri")
        }
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri {
        Log.d("ContentProvider", "insert()")

        val id = database.insert(TABLE_NAME, null, values)

        if (id > 0) {
            val uri2 = ContentUris.withAppendedId(uri, id)
            appContext.contentResolver.notifyChange(uri2, null)
            return uri2
        }

        throw SQLException("추가 실패 URI : $uri")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d("ContentProvider", "delete()")

        var count = 0
        when (uriMatcher.match(uri)) {
            STATUS -> count = database.delete(TABLE_NAME, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI : $uri")
        }
        appContext.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d("ContentProvider", "update()")

        val count =
            when (uriMatcher.match(uri)) {
                STATUS -> database.update(TABLE_NAME, values, selection, selectionArgs)
                else -> throw IllegalArgumentException("Unknown URI : $uri")
            }
        appContext.contentResolver.notifyChange(uri, null)
        return count
    }

    private fun updateAgentStatus(
        uri: Uri,
        agentStatus: String
    ) {
        this.agentStatus = agentStatus
        appContext.contentResolver.notifyChange(uri, null)
    }

    override fun call(
        method: String,
        arg: String?,
        extras: Bundle?
    ): Bundle? {
        Log.d("ContentProvider", "method:$method, extras:$extras")
        return when (method) {
            "updateAgentStatus" -> {
                val uri = extras?.getString("uri") ?: ""
                val agentStatus = extras?.getString("agentStatus") ?: ""
                updateAgentStatus(Uri.parse(uri), agentStatus)
                Bundle().apply {
                    putString("result", "success")
                }
            }
            "getAgentStatus" -> {
                Bundle().apply {
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("status", agentStatus)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    putString("result", jsonObject.toString())
                }
            }
            else -> null
        }
    }

    companion object {
        private const val AUTHORITY = "ai.umos.aiassistant.provider"
        private const val BASE_PATH = "status"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$BASE_PATH")

        private const val STATUS = 1
        private const val STATUS_ID = 2

        val uriMatcher =
            UriMatcher(UriMatcher.NO_MATCH).apply {
                addURI(AUTHORITY, BASE_PATH, STATUS)
                addURI(AUTHORITY, "$BASE_PATH/#", STATUS_ID)
            }
    }
}
