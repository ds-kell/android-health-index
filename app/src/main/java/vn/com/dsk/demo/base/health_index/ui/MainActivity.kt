package vn.com.dsk.demo.base.health_index.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import vn.com.dsk.demo.base.health_index.R
import vn.com.dsk.demo.base.health_index.common.JsonReader
import vn.com.dsk.demo.base.health_index.common.defuzzification
import vn.com.dsk.demo.base.health_index.common.fuzzyInference
import vn.com.dsk.demo.base.health_index.common.predict
import vn.com.dsk.demo.base.health_index.databinding.ActivityMainBinding
import vn.com.dsk.demo.base.health_index.model.ModelInput
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    private val viewModel: HealthIndexViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).apply {

            val jsonReader = JsonReader(this@MainActivity)
            val fileNameBp = "model_bp.json"
            val fileNameBs = "model_bp.json"
            val modelParametersBp = jsonReader.readJsonFile(fileNameBp)
            val modelParametersBs = jsonReader.readJsonFile(fileNameBs)
            btnSubmit.setOnClickListener {
                val stateContinue = !(ageInput.editTextInput.text.isNullOrEmpty() ||
                        bloodSugarInput.editTextInput.text.isNullOrEmpty() ||
                        diastolicBloodPressureInput.editTextInput.text.isNullOrEmpty() ||
                        systolicBloodPressureInput.editTextInput.text.isNullOrEmpty())
                if (stateContinue) {
                    textMessage.visibility = View.GONE
                    val modelInput = ModelInput(
                        age = ageInput.editTextInput.text.toString().toDouble(),
                        bs = bloodSugarInput.editTextInput.text.toString().toDouble(),
                        dbp = diastolicBloodPressureInput.editTextInput.text.toString().toDouble(),
                        sbp = systolicBloodPressureInput.editTextInput.text.toString().toDouble()
                    )
                    if (validateInput(modelInput)) {
                        textMessage.visibility = View.GONE
                        val modelPredict = predict(
                            bp = listOf(modelInput.age, modelInput.dbp, modelInput.sbp),
                            bs = listOf(modelInput.age, modelInput.bs),
                            parameterBp = modelParametersBp,
                            parameterBs = modelParametersBs,
                        )
                        val modelHealthIndex = fuzzyInference(modelPredict)
                        Log.d("kell-log", "$modelHealthIndex")
                        val healthIndex = round(defuzzification(modelHealthIndex) * 100) / 100
                        result.apply {
                            bloodSugar = modelPredict.getPredictValue().second
                            bloodPressure = modelPredict.getPredictValue().first
                            result.healthIndex = healthIndex.toString()
                            healthLabel = modelHealthIndex.getHealthKey()
                            healthRecommendations =
                                if (modelHealthIndex.getHealthKey() == "Healthy") {
                                    txtRecommendations.setTextColor(
                                        ContextCompat.getColor(this@MainActivity, R.color.green)
                                    )
                                    getString(R.string.recommendations_for_healthy)
                                } else {
                                    txtRecommendations.setTextColor(
                                        ContextCompat.getColor(this@MainActivity, R.color.red)
                                    )
                                    getString(R.string.recommendations_for_no_healthy)
                                }
                            layoutResult.visibility = View.VISIBLE
                        }
                        hideKeyboard()
                    } else {
                        textMessage.visibility = View.VISIBLE
                    }
                } else {
                    textMessage.visibility = View.VISIBLE
                }
            }
        }.root)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun validateInput(modelInput: ModelInput): Boolean =
        !(modelInput.age < 15 || modelInput.age > 55) &&
                !(modelInput.bs > 200 || modelInput.bs < 0) &&
                !(modelInput.sbp < modelInput.dbp || modelInput.dbp < 0 || modelInput.sbp > 200)

}