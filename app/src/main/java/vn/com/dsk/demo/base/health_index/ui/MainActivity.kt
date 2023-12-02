package vn.com.dsk.demo.base.health_index.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import vn.com.dsk.demo.base.health_index.common.JsonReader
import vn.com.dsk.demo.base.health_index.common.predict
import vn.com.dsk.demo.base.health_index.common.transposeMatrix
import vn.com.dsk.demo.base.health_index.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).apply {

            val jsonReader = JsonReader(this@MainActivity)
            val fileName = "model.json"
            val modelParameters = jsonReader.readJsonFromAssets(fileName)
            if (modelParameters != null) {
                btnSubmit.setOnClickListener {
                    val input = listOf(listOf(59.0, 170.0, 85.0, 108.0))
                    val X_test = transposeMatrix(input)
                    val res = predict(
                        X = X_test,
                        parameters = modelParameters
                    )
                    val displayText = listOf(
                        "X1: ${res.X1}",
                        "X2: ${res.X2}",
                        "X3: ${res.X3}",
                        "Y1: ${res.Y1}",
                        "Y2: ${res.Y2}",
                        "Y3: ${res.Y3}"
                    ).joinToString(" - ")
                    txtResult.visibility = View.VISIBLE
                    txtResult.text = displayText
                    Log.d("kell-log", "$res")
                }
            } else {
                txtResult.text = "Not found model"
            }
        }.root)
    }
}