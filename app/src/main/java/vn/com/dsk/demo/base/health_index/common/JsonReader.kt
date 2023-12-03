package vn.com.dsk.demo.base.health_index.common

import android.content.Context
import com.google.gson.Gson
import java.io.*

class JsonReader(private val context: Context) {
    fun readJsonFile(fileName: String): ModelParameters {
        val inputStream: InputStream = context.assets.open(fileName)
        val jsonString = readFromStream(inputStream)
        return Gson().fromJson(jsonString, ModelParameters::class.java)
    }

    private fun readFromStream(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return stringBuilder.toString()
    }
}

