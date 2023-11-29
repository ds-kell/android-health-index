package vn.com.dsk.demo.base.health_index.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.json.Json
import vn.com.dsk.demo.base.health_index.common.JsonReader
import vn.com.dsk.demo.base.health_index.common.ModelParameters
import vn.com.dsk.demo.base.health_index.common.predict
import vn.com.dsk.demo.base.health_index.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).apply {

            val jsonReader = JsonReader(this@MainActivity)
            val fileName = "model.json"
            val modelParameters = jsonReader.readJsonFromAssets(fileName)
            Log.d("kel", "$modelParameters")
            if (modelParameters != null) {
                btnSubmit.setOnClickListener {
                    val index = listOf(
                        age.text.toString().toDouble(),
                        bg.text.toString().toDouble(),
                        bp1.text.toString().toDouble(),
                        bp2.text.toString().toDouble()
                    )
                    val res = predict(listOf(index), modelParameters)
                    resBg.text = res.bloodSugar.toString()
                    resBp.text = res.bloodPressure.toString()
                }
            } else {
                resBg.text = "res.bloodSugar.toString()"
                resBp.text = "res.bloodPressure.toString()"
            }
        }.root)
    }
}