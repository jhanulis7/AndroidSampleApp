package ai.umos.aiassistant.provider

import ai.umos.aiassistant.provider.AiAssistantContract.COLUMN_STATUS
import ai.umos.aiassistant.provider.AiAssistantContract.TABLE_NAME
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DatabaseHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    companion object{
        const val DATABASE_NAME = "ai_assistant.db"
        const val DATABASE_VERSION = 1
        private var instance : DatabaseHelper? = null

        const val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_STATUS TEXT)"

        fun getInstance(context: Context): DatabaseHelper =
            if (instance == null) DatabaseHelper(context) else instance!!
    }
}