package vn.com.dsk.demo.base.health_index.test

import kotlinx.serialization.json.Json
import java.io.File
import kotlin.math.exp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun sigmoid(x: Double): Double {
    return 1.0 / (1.0 + exp(-x))
}

fun softmax(x: List<Double>): List<Double> {
    val expX = x.map { exp(it) }
    val sumExpX = expX.sum()
    return expX.map { it / sumExpX }
}

fun forwardPropagation(X: List<Double>, parameters: ModelParameters): Map<String, List<Double>> {
    val Z1 = X.zip(parameters.w1[0]) { x, w -> x * w }.sum() + parameters.b1[0]
    val A1 = sigmoid(Z1)

    val Z2Bp = A1.zip(parameters.w2Bp[0]) { a, w -> a * w }.sum() + parameters.b2Bp[0]
    val A2Bp = softmax(listOf(Z2Bp))

    val Z2Bg = A1.zip(parameters.w2Bg[0]) { a, w -> a * w }.sum() + parameters.b2Bg[0]
    val A2Bg = softmax(listOf(Z2Bg))

    return mapOf(
        "Z1" to listOf(Z1),
        "A1" to listOf(A1),
        "Z2Bp" to listOf(Z2Bp),
        "A2Bp" to A2Bp,
        "Z2Bg" to listOf(Z2Bg),
        "A2Bg" to A2Bg
    )
}

fun predict(X: List<Double>, parameters: ModelParameters): Pair<Int, Int> {
    val cache = forwardPropagation(X, parameters)
    val predictionBp = cache["A2Bp"]!!.indexOf(cache["A2Bp"]!!.max()!!)
    val predictionBg = cache["A2Bg"]!!.indexOf(cache["A2Bg"]!!.max()!!)
    return Pair(predictionBp, predictionBg)
}

fun main() {
    val jsonString =
        File("D:\\truyen.nguyentuan\\kell-android-project\\health-index\\app\\src\\main\\java\\vn\\com\\dsk\\demo\\base\\health_index\\test\\model.json").readText()
    val modelParameters = Json.decodeFromString<ModelParameters>(jsonString)
}

@Serializable
data class ModelParameters(
    @SerialName("W1") val w1: List<List<Double>>,
    @SerialName("b1") val b1: List<Double>,
    @SerialName("W2_bp") val w2Bp: List<List<Double>>,
    @SerialName("b2_bp") val b2Bp: List<Double>,
    @SerialName("W2_bg") val w2Bg: List<List<Double>>,
    @SerialName("b2_bg") val b2Bg: List<Double>
)
