package vn.com.dsk.demo.base.health_index.model

object MatrixRule {
    val rule = mutableMapOf(
        "Low" to mutableMapOf(
            "Low" to "Unhealthy",
            "Medium" to "Seems Healthy",
            "High" to "Unhealthy"
        ),
        "Medium" to mutableMapOf(
            "Low" to "Seems Healthy",
            "Medium" to "Healthy",
            "High" to "Low Healthy"
        ),
        "High" to mutableMapOf(
            "Low" to "Unhealthy",
            "Medium" to "Low Healthy",
            "High" to "Unhealthy"
        )
    )
}


data class ModelParameters(
    val weights: List<List<Double>>, val bias: List<Double>
)

data class ModelPredict(
    val bloodPressure: ModelLevel = ModelLevel(
        low = ModelKeyValue("Low", 0.0),
        medium = ModelKeyValue("Medium", 0.0),
        high = ModelKeyValue("High", 0.0)
    ), val bloodSugar: ModelLevel = ModelLevel(
        low = ModelKeyValue("Low", 0.0),
        medium = ModelKeyValue("Medium", 0.0),
        high = ModelKeyValue("High", 0.0)
    )
) {
    fun getPredictValue(): Pair<String, String> {
        val bloodPressureLevel = listOf(
            bloodPressure.low,
            bloodPressure.medium,
            bloodPressure.high
        )

        val bloodSugarLevel = listOf(
            bloodSugar.low,
            bloodSugar.medium,
            bloodSugar.high
        )
        val maxBloodPressureValue = bloodPressureLevel.maxByOrNull { it.value }
        val maxBloodSugarValue = bloodSugarLevel.maxByOrNull { it.value }

        return Pair(maxBloodPressureValue?.key ?: "", maxBloodSugarValue?.key ?: "")
    }
}

data class ModelLevel(
    val low: ModelKeyValue,
    val medium: ModelKeyValue,
    val high: ModelKeyValue,
)

data class ModelKeyValue(
    var key: String = "",
    var value: Double = 0.0,
)

data class ModelHealthIndex(
    val unhealthy: ModelKeyValue = ModelKeyValue(key = "Unhealthy", value = 0.0),
    val lowHealthy: ModelKeyValue = ModelKeyValue(key = "Low Healthy", value = 0.0),
    val seemsHealthy: ModelKeyValue = ModelKeyValue(key = "Seems Healthy", value = 0.0),
    val healthy: ModelKeyValue = ModelKeyValue(key = "Healthy", value = 0.0)
) {
    fun getHealthKey(): String {
        val keyValueList = listOf(unhealthy, lowHealthy, seemsHealthy, healthy)
        val maxKeyValue = keyValueList.maxByOrNull { it.value }
        return maxKeyValue?.key ?: ""
    }
}

data class ModelInput(
    val age: Double = 0.0,
    val bs: Double = 0.0,
    val dbp: Double = 0.0,
    val sbp: Double = 0.0,
)