import kotlin.math.exp
import kotlin.math.argmax
import kotlin.random.Random

fun sigmoid(x: Double): Double {
    return 1 / (1 + exp(-x))
}

fun softmax(x: DoubleArray): DoubleArray {
    val maxVal = x.maxOrNull() ?: 0.0
    val expX = x.map { exp(it - maxVal) }.toDoubleArray()
    val sumExpX = expX.sum()
    return expX.map { it / sumExpX }.toDoubleArray()
}

fun forwardPropagationMultiOutput(X: Array<DoubleArray>, parameters: Map<String, DoubleArray>): Map<String, DoubleArray> {
    val W1 = parameters["W1"] ?: doubleArrayOf()
    val b1 = parameters["b1"] ?: doubleArrayOf()
    val W2_bp = parameters["W2_bp"] ?: doubleArrayOf()
    val b2_bp = parameters["b2_bp"] ?: doubleArrayOf()
    val W2_bg = parameters["W2_bg"] ?: doubleArrayOf()
    val b2_bg = parameters["b2_bg"] ?: doubleArrayOf()

    val Z1 = Array(X.size) { DoubleArray(W1[0].size) }
    val A1 = Array(X.size) { DoubleArray(W1[0].size) }
    val Z2_bp = Array(X.size) { DoubleArray(W2_bp[0].size) }
    val A2_bp = Array(X.size) { DoubleArray(W2_bp[0].size) }
    val Z2_bg = Array(X.size) { DoubleArray(W2_bg[0].size) }
    val A2_bg = Array(X.size) { DoubleArray(W2_bg[0].size) }

    for (i in X.indices) {
        for (j in W1[0].indices) {
            for (k in X[0].indices) {
                Z1[i][j] += X[i][k] * W1[k][j]
            }
            Z1[i][j] += b1[j]
            A1[i][j] = sigmoid(Z1[i][j])
        }
    }

    for (i in A1.indices) {
        for (j in W2_bp[0].indices) {
            for (k in A1[0].indices) {
                Z2_bp[i][j] += A1[i][k] * W2_bp[k][j]
            }
            Z2_bp[i][j] += b2_bp[j]
            A2_bp[i][j] = softmax(Z2_bp[i])[j]
        }
    }

    for (i in A1.indices) {
        for (j in W2_bg[0].indices) {
            for (k in A1[0].indices) {
                Z2_bg[i][j] += A1[i][k] * W2_bg[k][j]
            }
            Z2_bg[i][j] += b2_bg[j]
            A2_bg[i][j] = softmax(Z2_bg[i])[j]
        }
    }

    return mapOf(
        "Z1" to Z1,
        "A1" to A1,
        "Z2_bp" to Z2_bp,
        "A2_bp" to A2_bp,
        "Z2_bg" to Z2_bg,
        "A2_bg" to A2_bg
    )
}

fun predictMultiOutput(X: Array<DoubleArray>, parameters: Map<String, DoubleArray>): Pair<IntArray, IntArray> {
    val cache = forwardPropagationMultiOutput(X, parameters)
    val A2_bp = cache["A2_bp"] ?: doubleArrayOf()
    val A2_bg = cache["A2_bg"] ?: doubleArrayOf()

    val predictions_bp = IntArray(A2_bp.size) { argmax(A2_bp[it]) }
    val predictions_bg = IntArray(A2_bg.size) { argmax(A2_bg[it]) }

    return Pair(predictions_bp, predictions_bg)
}

val pathToJsonFile = "model.json"

val parametersMultiOutput = mutableMapOf<String, DoubleArray>()

val file = File(pathToJsonFile)
file.bufferedReader().use { reader ->
    val jsonContent = reader.readText()
    val jsonObject = JSONObject(jsonContent)

    for (key in jsonObject.keys()) {
        val jsonArray = jsonObject.getJSONArray(key)
        val doubleArray = DoubleArray(jsonArray.length())
        for (i in 0 until jsonArray.length()) {
            doubleArray[i] = jsonArray.getDouble(i)
        }
        parametersMultiOutput[key] = doubleArray
    }
}

val dataNew = mapOf(
    "Age" to List(10) { Random.nextInt(20, 80).toDouble() },
    "BGL-F" to List(10) { Random.nextDouble(70.0, 200.0) },
    "Diastolic_BP" to List(10) { Random.nextInt(60, 90).toDouble() },
    "Systolic_BP" to List(10) { Random.nextInt(90, 140).toDouble() }
)

val dfNew = dataNew.toDataFrame()

val XNew = dfNew.select("Age", "BGL-F", "Diastolic_BP", "Systolic_BP").toMatrix()

val (predictionsBpNew, predictionsBgNew) = predictMultiOutput(XNew, parametersMultiOutput)

println(predictionsBpNew.contentToString())
println(predictionsBgNew.contentToString())


