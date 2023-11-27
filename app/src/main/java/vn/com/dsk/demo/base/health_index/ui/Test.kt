package vn.com.dsk.demo.base.health_index.ui

fun main() {
    // Tạo một số dữ liệu để test
    val testInput = listOf(
        listOf(30.0, 120.0, 70.0, 110.0, 80.0),
        listOf(40.0, 130.0, 75.0, 120.0, 85.0),
    )

    val numHiddenUnits = 64
    val numClassesBP = 3
    val numClassesBG = 3

    val parameters = ModelParameters(
        W1 = List(testInput[0].size) { List(numHiddenUnits) { 0.1 } },
        b1 = List(numHiddenUnits) { 0.1 },
        W2_bp = List(numHiddenUnits) { List(numClassesBP) { 0.1 } },
        b2_bp = List(numClassesBP) { 0.1 },
        W2_bg = List(numHiddenUnits) { List(numClassesBG) { 0.1 } },
        b2_bg = List(numClassesBG) { 0.1 }
    )

    val forwardCache = forwardPropagationMultiOutput(testInput, parameters)

    // Display the results
    println("Forward Cache:")
    println("Z1: ${forwardCache.Z1}")
    println("A1: ${forwardCache.A1}")
    println("Z2_BP: ${forwardCache.Z2_bp}")
    println("A2_BP: ${forwardCache.A2_bp}")
    println("Z2_BG: ${forwardCache.Z2_bg}")
    println("A2_BG: ${forwardCache.A2_bg}")

    val predictions = predict(forwardCache.A2_bg)

    println("Predictions: $predictions")
}