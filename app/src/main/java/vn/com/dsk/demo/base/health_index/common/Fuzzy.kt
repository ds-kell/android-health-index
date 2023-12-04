package vn.com.dsk.demo.base.health_index.common

import vn.com.dsk.demo.base.health_index.model.MatrixRule
import vn.com.dsk.demo.base.health_index.model.ModelHealthIndex
import vn.com.dsk.demo.base.health_index.model.ModelPredict


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
                "Unhealthy" -> modelHealthIndex.unhealthy.value = maxOf(minValue, modelHealthIndex.unhealthy.value)
                "Low Healthy" -> modelHealthIndex.lowHealthy.value = maxOf(minValue, modelHealthIndex.lowHealthy.value)
                "Seems Healthy" -> modelHealthIndex.seemsHealthy.value = maxOf(minValue, modelHealthIndex.seemsHealthy.value)
                "Healthy" -> modelHealthIndex.healthy.value = maxOf(minValue, modelHealthIndex.healthy.value)
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