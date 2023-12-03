package vn.com.dsk.demo.base.health_index.common

object MatrixRule {
    val rule = mutableMapOf(
        "Low"    to mutableMapOf("Low" to "UH", "Medium" to "SH", "High" to "UH"),
        "Medium" to mutableMapOf("Low" to "SH", "Medium" to "H",  "High" to "LH"),
        "High"   to mutableMapOf("Low" to "UH", "Medium" to "LH", "High" to "UH")
    )
}

fun fuzzyInference(modelPredict: ModelPredict): ModelHealthIndex {
    val modelHealthIndex = ModelHealthIndex()
    for ((pressureLevel, sugarLevelMap) in MatrixRule.rule) {
        val pressureValue = modelPredict.bloodPressure.run {
            when (pressureLevel) {
                "Low" -> low.value
                "Medium" -> medium.value
                "High" -> high.value
                else -> 0.0
            }
        }

        for ((sugarLevel, healthIndexKey) in sugarLevelMap) {
            val sugarValue = modelPredict.bloodSugar.run {
                when (sugarLevel) {
                    "Low" -> low.value
                    "Medium" -> medium.value
                    "High" -> high.value
                    else -> 0.0
                }
            }

            val minValue = minOf(pressureValue, sugarValue)

            when (healthIndexKey) {
                "UH" -> modelHealthIndex.unhealthy.value = maxOf(minValue, modelHealthIndex.unhealthy.value)
                "LH" -> modelHealthIndex.lowHealthy.value = maxOf(minValue, modelHealthIndex.lowHealthy.value)
                "SH" -> modelHealthIndex.seemsHealthy.value = maxOf(minValue, modelHealthIndex.seemsHealthy.value)
                "H" -> modelHealthIndex.healthy.value = maxOf(minValue, modelHealthIndex.healthy.value)
            }
        }
    }
    return modelHealthIndex
}


fun defuzzification(modelHealthIndex: ModelHealthIndex): Double {
    val weightUnhealthy = 0.2
    val weightLowHealthy = 0.4
    val weightSeemsHealthy = 0.6
    val weightHealthy = 0.8

    val totalWeightedSum = (modelHealthIndex.unhealthy.value * weightUnhealthy +
            modelHealthIndex.lowHealthy.value * weightLowHealthy +
            modelHealthIndex.seemsHealthy.value * weightSeemsHealthy +
            modelHealthIndex.healthy.value * weightHealthy)

    val totalWeight = (modelHealthIndex.unhealthy.value +
            modelHealthIndex.lowHealthy.value +
            modelHealthIndex.seemsHealthy.value +
            modelHealthIndex.healthy.value)

    return if (totalWeight != 0.0) {
        totalWeightedSum / totalWeight
    } else {
        0.0
    }
}

fun main() {
    val modelPredict = ModelPredict(
        ModelLevel(
            low = ModelKeyValue("Low", 0.8),
            medium = ModelKeyValue("Medium", 0.0),
            high = ModelKeyValue("High", 0.0)
        ), ModelLevel(
            low = ModelKeyValue("Low", 0.0),
            medium = ModelKeyValue("Medium", 0.0),
            high = ModelKeyValue("High", 0.0)
        )
    )
}