package vn.com.dsk.demo.base.health_index.common

import kotlin.math.*


fun matrixMultiplication(
    matrix1: List<List<Double>>, matrix2: List<List<Double>>
): List<List<Double>> {
    val row1 = matrix1.size
    val col1 = matrix1[0].size
    val col2 = matrix2[0].size

    val result = MutableList(row1) { MutableList(col2) { 0.0 } }

    for (i in 0 until row1) {
        for (j in 0 until col2) {
            for (k in 0 until col1) {
                result[i][j] += matrix1[i][k] * matrix2[k][j]
            }
        }
    }

    return result
}

fun normalizeArray(inputArray: List<List<Double>>): List<List<Double>> {

    if (inputArray.size != 1) {
        return inputArray
    }

    val partSize = inputArray[0].size / 2
    val firstPart = inputArray[0].subList(0, partSize)
    val secondPart = inputArray[0].subList(partSize, inputArray[0].size)

    return listOf(roundArray(firstPart), roundArray(secondPart))
}

fun roundArray(listInput: List<Double>): List<Double> {
    if (listInput.isEmpty()) return emptyList()
    val tmp = listInput.map { if (it < 0) 0.0 else it }
    val minValue = tmp.minOrNull() ?: 0.0
    val roundedList = tmp.map {
        if (it == minValue)
            0.0
        else
            it + minValue / (tmp.size - 1)

    }
    val sum = roundedList.sum()
    return if (sum != 0.0) {
        roundedList.map { round(it / sum * 1000.0) / 1000.0 }
    } else {
        roundedList
    }
}


fun predict(X: List<Double>, parameters: ModelParameters): ModelPredict {
    val tmp1 = matrixMultiplication(listOf(X), transposeMatrix(parameters.weights))
    val tmp2 = matrixAddition(tmp1, listOf(parameters.bias))
    val result = normalizeArray(tmp2)
    return ModelPredict(
        bloodPressure = ModelLevel(
            low = ModelKeyValue("Low", result[0][0]),
            medium = ModelKeyValue("Medium", result[0][1]),
            high = ModelKeyValue("High", result[0][2])
        ), bloodSugar = ModelLevel(
            low = ModelKeyValue("Low", result[1][0]),
            medium = ModelKeyValue("Medium", result[1][1]),
            high = ModelKeyValue("High", result[1][2])
        )
    )
}


fun matrixAddition(matrix1: List<List<Double>>, matrix2: List<List<Double>>): List<List<Double>> {
    require(matrix1.size == matrix2.size && matrix1[0].size == matrix2[0].size) { "Matrices must have the same dimensions for addition." }

    val numRows = matrix1.size
    val numCols = matrix1[0].size

    val result = MutableList(numRows) { MutableList(numCols) { 0.0 } }

    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            result[i][j] = matrix1[i][j] + matrix2[i][j]
        }
    }

    return result
}

fun transposeMatrix(matrix: List<List<Double>>): List<List<Double>> {
    val rows = matrix.size
    val cols = matrix[0].size

    val result = MutableList(cols) { MutableList(rows) { 0.0 } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            result[j][i] = matrix[i][j]
        }
    }

    return result
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
)

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
    val unhealthy: ModelKeyValue = ModelKeyValue(key = "UH", value = 0.0),
    val lowHealthy: ModelKeyValue = ModelKeyValue(key = "LH", value = 0.0),
    val seemsHealthy: ModelKeyValue = ModelKeyValue(key = "SH", value = 0.0),
    val healthy: ModelKeyValue = ModelKeyValue(key = "H", value = 0.0)
)