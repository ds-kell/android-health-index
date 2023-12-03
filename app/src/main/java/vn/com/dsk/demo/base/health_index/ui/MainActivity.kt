package vn.com.dsk.demo.base.health_index.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import vn.com.dsk.demo.base.health_index.common.JsonReader
import vn.com.dsk.demo.base.health_index.common.defuzzification
import vn.com.dsk.demo.base.health_index.common.fuzzyInference
import vn.com.dsk.demo.base.health_index.common.predict
import vn.com.dsk.demo.base.health_index.databinding.ActivityMainBinding
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).apply {

            val jsonReader = JsonReader(this@MainActivity)
            val fileName = "model.json"
            val modelParameters = jsonReader.readJsonFile(fileName)
            ageInput.apply {
                textLabel = "Tuổi của bạn"
                textUnit = "Tuổi (năm)"
                textHint = "Tuổi của bạn"
            }
            diastolicBloodPressureInput.apply {
                textHint = "Huyết áp tâm trương"
                textUnit = "mmHg"
            }
            systolicBloodPressureInput.apply {
                textHint = "Huyết áp tâm thu"
                textUnit = "mmHg"
            }
            bloodSugarInput.apply {
                textHint = "Đường huyết khi đói"
                textUnit = " mg/dL"
            }
            btnSubmit.setOnClickListener {
                val modelPredict = predict(
                    X = listOf(18.0, 130.0, 88.0, 130.0),
                    parameters = modelParameters
                )
                val health = fuzzyInference(modelPredict)
                val healthIndex = round(defuzzification(health) * 100) / 100
                txtHealthIndex.visibility = View.VISIBLE
                txtHealth.visibility = View.VISIBLE
                txtRecommendations.visibility = View.VISIBLE
                txtLabelRecommendations.visibility = View.VISIBLE
                txtHealth.text = "Tình trạng sức khoẻ của bạn: ${healthIndex}"
                txtHealthIndex.text =
                    "Chỉ số sức khoẻ: ${healthIndex}"
                txtLabelRecommendations.text = "Lời khuyên*"
                txtRecommendations.text =
                    "Bạn đang có sức khoẻ ${healthIndex}. Bạn cần nhanh chóng tới cơ sở y tế khám và điều trị"
                hideKeyboard()
            }
        }.root)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}