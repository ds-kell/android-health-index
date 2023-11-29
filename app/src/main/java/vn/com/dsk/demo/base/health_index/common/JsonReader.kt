package vn.com.dsk.demo.base.health_index.common

import android.content.Context
import android.content.res.AssetManager
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class JsonReader(private val context: Context) {

    fun readJsonFromAssets(fileName: String): ModelParameters? {
        var json: String? = null
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charset.defaultCharset())
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

