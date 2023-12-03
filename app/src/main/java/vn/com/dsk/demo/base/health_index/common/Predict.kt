package vn.com.dsk.demo.base.health_index.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.*


fun sigmoid(matrix: List<List<Double>>): List<List<Double>> {
    return matrix.map { row ->
        row.map { element ->
            1.0 / (1.0 + exp(-element))
        }
    }
}

fun softmax(matrix: List<List<Double>>): List<List<Double>> {
    return matrix.map { row ->
        val rowExp = row.map { exp(it) }
        val sumExp = rowExp.sum()
        rowExp.map { it / sumExp }
    }
}

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

fun predict(X: List<List<Double>>, parameters: ModelParameters): ModelPredict {
    val forwardPropagation = forwardPropagationMultiOutput(X, parameters)
    return ModelPredict(
        bloodPressure = ModelLevel(
            low = ModelKeyValue("Low", forwardPropagation.A2[0][0]),
            medium = ModelKeyValue("Medium", forwardPropagation.A2[1][0]),
            high = ModelKeyValue("High", forwardPropagation.A2[2][0])
        ), bloodSugar = ModelLevel(
            low = ModelKeyValue("Low", forwardPropagation.A2[3][0]),
            medium = ModelKeyValue("Medium", forwardPropagation.A2[4][0]),
            high = ModelKeyValue("High", forwardPropagation.A2[4][0])
        )
    )
}

fun forwardPropagationMultiOutput(
    X: List<List<Double>>, parameters: ModelParameters
): ModelForwardPropagation {

    val Z1 = matrixAddition(matrixMultiplication(parameters.W1, X), parameters.b1)
    val A1 = sigmoid(Z1)

    val Z2 = matrixAddition(matrixMultiplication(parameters.W2, A1), parameters.b2)
    val A2 = sigmoid(Z2)


    return ModelForwardPropagation(
        Z1 = Z1,
        A1 = A1,
        Z2 = Z2,
        A2 = A2,
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

@Serializable
data class ModelParameters(
    @SerialName("W1") val W1: List<List<Double>>,
    @SerialName("b1") val b1: List<List<Double>>,
    @SerialName("W2") val W2: List<List<Double>>,
    @SerialName("b2") val b2: List<List<Double>>,
)

data class ModelForwardPropagation(
    val Z1: List<List<Double>>,
    val A1: List<List<Double>>,
    val Z2: List<List<Double>>,
    val A2: List<List<Double>>,
)

data class ModelPredict(
    val bloodPressure: ModelLevel = ModelLevel(
        low = ModelKeyValue("Low", 0.0),
        medium = ModelKeyValue("Medium", 0.0),
        high = ModelKeyValue("High", 0.0)
    ),
    val bloodSugar: ModelLevel = ModelLevel(
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