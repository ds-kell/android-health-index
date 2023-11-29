package vn.com.dsk.demo.base.health_index.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
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
    matrix1: List<List<Double>>,
    matrix2: List<List<Double>>
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

fun dotProduct(matrix1: List<List<Double>>, matrix2: List<List<Double>>): Double {
    require(matrix1.size == matrix2.size && matrix1[0].size == matrix2[0].size) { "Matrices must have the same dimensions for dot product." }

    val numRows = matrix1.size
    val numCols = matrix1[0].size

    var result = 0.0

    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            result += matrix1[i][j] * matrix2[i][j]
        }
    }

    return result
}

fun predict(X: List<List<Double>>, parameters: ModelParameters): ModelPredict {
    val forwardPropagation = forwardPropagationMultiOutput(X, parameters)

    val bgLabels = argmax(forwardPropagation.A2_bg).map { mapToLabel(it) }
    val bpLabels = argmax(forwardPropagation.A2_bp).map { mapToLabel(it) }

    return ModelPredict(bloodPressure = bgLabels, bloodSugar = bpLabels)
}

fun forwardPropagationMultiOutput(
    X: List<List<Double>>,
    parameters: ModelParameters
): ModelForwardPropagation {
//    Hidden layer
    val Z1 = matrixAddition(matrixMultiplication(X, parameters.w1), parameters.b1)
    val A1 = sigmoid(Z1)

//    Blood Pressure output layer
    val Z2_bp = matrixAddition(matrixMultiplication(A1, parameters.w2Bp), parameters.b2Bp)
    val A2_bp = softmax(Z2_bp)

//    Blood Glucose output layer
    val Z2_bg = matrixAddition(matrixMultiplication(A1, parameters.w2Bg), parameters.b2Bg)
    val A2_bg = softmax(Z2_bg)

    return ModelForwardPropagation(
        Z1 = Z1,
        A1 = A1,
        Z2_bp = Z2_bp,
        A2_bp = A2_bp,
        Z2_bg = Z2_bg,
        A2_bg = A2_bg
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

fun printMatrix(matrix: List<List<Double>>) {
    for (row in matrix) {
        for (element in row) {
            print("$element\t")
        }
        println()
    }
}
//
//fun main() {
//
//    val jsonString =
//        File("model.json").readText()
//    val modelParameters = Json.decodeFromString<ModelParameters>(jsonString)
//
//    val modelIndexList = ModelIndex(71.0, 123.120632, 78.0, 134.0)
//
//    val matrix2 = modelIndexList.let {
//        listOf(it.age, it.BGL_F, it.diastolic_BP, it.systolic_BP)
//    }
//    print(modelParameters)
//}


@Serializable
data class ModelIndex(
    @SerialName("age") val age: Double,
    @SerialName("BGL_F") val BGL_F: Double,
    @SerialName("diastolic_BP") val diastolic_BP: Double,
    @SerialName("systolic_BP") val systolic_BP: Double,
)

@Serializable
data class ModelParameters(
    @SerialName("W1") val w1: List<List<Double>>,
    @SerialName("b1") val b1: List<List<Double>>,
    @SerialName("W2_bp") val w2Bp: List<List<Double>>,
    @SerialName("b2_bp") val b2Bp: List<List<Double>>,
    @SerialName("W2_bg") val w2Bg: List<List<Double>>,
    @SerialName("b2_bg") val b2Bg: List<List<Double>>,
)

@Serializable
data class ModelForwardPropagation(
    @SerialName("Z1") val Z1: List<List<Double>>,
    @SerialName("A1") val A1: List<List<Double>>,
    @SerialName("Z2_bp") val Z2_bp: List<List<Double>>,
    @SerialName("A2_bp") val A2_bp: List<List<Double>>,
    @SerialName("Z2_bg") val Z2_bg: List<List<Double>>,
    @SerialName("A2_bg") val A2_bg: List<List<Double>>,
)

@Serializable
data class ModelPredict(
    @SerialName("bg") val bloodPressure: List<String>,
    @SerialName("bp") val bloodSugar: List<String>,
)

fun argmax(matrix: List<List<Double>>): List<Int> {
    return matrix.map { row ->
        row.indexOf(row.maxOrNull())
    }
}

fun mapToLabel(value: Int): String {
    return when (value) {
        0 -> "Low"
        1 -> "Medium"
        2 -> "High"
        else -> throw IllegalArgumentException("Invalid value: $value")
    }
}