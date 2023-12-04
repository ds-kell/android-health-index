package vn.com.dsk.demo.base.health_index.common

import android.util.Log
import vn.com.dsk.demo.base.health_index.model.ModelKeyValue
import vn.com.dsk.demo.base.health_index.model.ModelLevel
import vn.com.dsk.demo.base.health_index.model.ModelParameters
import vn.com.dsk.demo.base.health_index.model.ModelPredict
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

fun normalizeArray(listInput: List<List<Double>>): List<Double> {
    if (listInput.isEmpty() || listInput[0].isEmpty()) return emptyList()
    val tmpList = listInput[0].toMutableList()
//    Log.d("kell-log", "bf: $tmpList")
    return tmpList
    /**var minValue = tmpList.minOrNull() ?: 0.0
    for (i in 0..<tmpList.size) {
        if (tmpList[i] < 0.0) {
            tmpList[i] = 0.0
            minValue = 0.0
        }
        if (minValue != 0.0 && tmpList[i] == minValue) {
            if (i == 0) {
                tmpList[0] = 0.0
                tmpList[1] = (tmpList[0] + tmpList[0])
            }
            if (i == tmpList.size - 1) {
                tmpList[i - 1] = tmpList[i - 1] + tmpList[i]
            }
        }
    }
    val sum = tmpList.sum()
    return if (sum != 0.0) {
        tmpList.map { value ->
            if (value == sum) {
                round(value * 1000) / 1000
            } else {
                round(value / sum * 1000.0) / 1000.0
            }
        }
    } else {
        tmpList
    }*/
}


fun predict(
    bp: List<Double>,
    bs: List<Double>,
    parameterBp: ModelParameters,
    parameterBs: ModelParameters
): ModelPredict {

    val tmpBp = matrixMultiplication(listOf(bp), transposeMatrix(parameterBp.weights))
    val tmpBs = matrixMultiplication(listOf(bs), transposeMatrix(parameterBs.weights))

    val resultBp = normalizeArray(matrixAddition(tmpBp, listOf(parameterBp.bias)))
    Log.d("kell-log", "bp: $resultBp")

    val resultBs = normalizeArray(matrixAddition(tmpBs, listOf(parameterBs.bias)))
    Log.d("kell-log", "bs: $resultBs")

    return ModelPredict(
        bloodPressure = ModelLevel(
            low = ModelKeyValue("Low", resultBp[0]),
            medium = ModelKeyValue("Medium", resultBp[1]),
            high = ModelKeyValue("High", resultBp[2])
        ), bloodSugar = ModelLevel(
            low = ModelKeyValue("Low", resultBs[0]),
            medium = ModelKeyValue("Medium", resultBs[1]),
            high = ModelKeyValue("High", resultBs[2])
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