package vn.com.dsk.demo.base.health_index.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
            if (modelParameters != null) {
                btnSubmit.setOnClickListener {
//                    val input = listOf(listOf(59.0, 170.0, 85.0, 108.0))
//                    val X_test = transposeMatrix(input)
//                    val res = predict(
//                        X = X_test,
//                        parameters = modelParameters
//                    )
                    txtHealthIndex.visibility = View.VISIBLE
                    txtHealth.visibility = View.VISIBLE
                    txtRecommendations.visibility = View.VISIBLE
                    txtLabelRecommendations.visibility = View.VISIBLE
                    txtHealth.text = "Tình trạng sức khoẻ của bạn: ${"Un Healthy"}"
                    txtHealthIndex.text = "Chỉ số sức khoẻ: ${0.15}. Tương đương: ${1.5}/10"
                    txtLabelRecommendations.text = "Lời khuyên*"
                    txtRecommendations.text =
                        "Bạn đang có sức khoẻ yếu. Bạn cần nhanh chóng tới cơ sở y tế khám và điều trị"
//                    Log.d("kell-log", "$res")
                    hideKeyboard()
                }
            } else {
                txtRecommendations.text = "Not found model"
            }
        }.root)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }}