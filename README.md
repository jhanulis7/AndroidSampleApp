# CpContentProvider

### AndroidManifest
```
    <uses-permission android:name="ai.umos.aiasistant.permission.READ_DATABASE"/>
    <queries>
        <provider android:authorities="ai.umos.aiassistant.provider"/>
        <package android:name="ai.umos.aiassistant"/>
    </queries>
```
### AiAssistantProvider.kt
- ContentProvider Uri Definition
```
    companion object {
        private const val AUTHORITY = "ai.umos.aiassistant.provider"
        private const val BASE_PATH = "status"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$BASE_PATH")
    }
```
- Query Uri 
```
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
```
- register contentObserve Uri Definition
```
    private fun registerContentProvider() {
        try {
            context.contentResolver.registerContentObserver(AiAssistantProvider.CONTENT_URI, false, contentObserver)
        } catch (e: SecurityException) {
            Log.e("ContentProvider", "$e")
        }
    }
```
- observe content reslove Uri 
```
    private val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            super.onChange(selfChange, uri)
            uri?.let {
                Log.d(tag, "uri:$it contentObserver changed selfChange:$selfChange ")
                queryAgentStatus()
            }
        }
    }
```

