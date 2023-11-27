package vn.com.dsk.demo.base.health_index.ui

import kotlin.math.exp

fun sigmoid(x: Double): Double {
    return 1 / (1 + exp(-x))
}

fun softmax(x: List<Double>): List<Double> {
    val expX = x.map { exp(it) }
    val sumExpX = expX.sum()
    return expX.map { it / sumExpX }
}

data class ModelParameters(
    val W1: List<List<Double>>,
    val b1: List<Double>,
    val W2_bp: List<List<Double>>,
    val b2_bp: List<Double>,
    val W2_bg: List<List<Double>>,
    val b2_bg: List<Double>
)

data class ForwardCache(
    val Z1: List<List<Double>>,
    val A1: List<List<Double>>,
    val Z2_bp: List<List<Double>>, // Các giá trị Z2 cho Blood Pressure
    val A2_bp: List<List<Double>>, // Các giá trị A2 cho Blood Pressure
    val Z2_bg: List<List<Double>>, // Các giá trị Z2 cho Blood Glucose
    val A2_bg: List<List<Double>>  // Các giá trị A2 cho Blood Glucose
)

fun forwardPropagationBP(X: List<List<Double>>, parameters: ModelParameters): ForwardCache {
    val Z1 = X.map { xi ->
        parameters.W1.zip(parameters.b1) { w, b ->
            xi.zip(w) { x, wi -> x * wi }.sum() + b
        }
    }
    val A1 = Z1.map { zi -> zi.map { sigmoid(it) } }

    val Z2BP = A1.map { ai ->
        parameters.W2_bp.zip(parameters.b2_bp) { w, b ->
            ai.zip(w) { a, wi -> a * wi }.sum() + b
        }
    }
    val A2BP = Z2BP.map { zi -> softmax(zi) }

    return ForwardCache(Z1, A1, Z2BP, A2BP, emptyList(), emptyList())
}

fun forwardPropagationBG(X: List<List<Double>>, parameters: ModelParameters): ForwardCache {
    val Z1 = X.map { xi ->
        parameters.W1.zip(parameters.b1) { w, b ->
            xi.zip(w) { x, wi -> x * wi }.sum() + b
        }
    }
    val A1 = Z1.map { zi -> zi.map { sigmoid(it) } }

    val Z2BG = A1.map { ai ->
        parameters.W2_bg.zip(parameters.b2_bg) { w, b ->
            ai.zip(w) { a, wi -> a * wi }.sum() + b
        }
    }
    val A2BG = Z2BG.map { zi -> softmax(zi) }

    return ForwardCache(Z1, A1, emptyList(), emptyList(), Z2BG, A2BG)
}

fun forwardPropagationMultiOutput(
    X: List<List<Double>>,
    parameters: ModelParameters
): ForwardCache {
    val cacheBP = forwardPropagationBP(X, parameters)
    val cacheBG = forwardPropagationBG(X, parameters)

    return ForwardCache(
        cacheBP.Z1 + cacheBG.Z1,
        cacheBP.A1 + cacheBG.A1,
        cacheBP.Z2_bp + cacheBG.Z2_bp,
        cacheBP.A2_bp + emptyList(),
        cacheBG.Z2_bg,
        cacheBG.A2_bg
    )
}

fun predict(predictions: List<List<Double>>): List<Int> {
    return predictions.map { it.indexOf(it.maxOrNull() ?: 0.0) }
}

fun main() {
    // Tạo một số dữ liệu để test
    val testInput = listOf(
        listOf(30.0, 120.0, 70.0, 110.0, 80.0),   // Example input 1
        listOf(40.0, 130.0, 75.0, 120.0, 85.0),   // Example input 2
        // ... Add more examples as needed
    )

    // Thay đổi kích thước của trọng số để phản ánh số lượng thuộc tính
    val numHiddenUnits = 64
    val numClassesBP = 3  // Số lớp cho Blood Pressure
    val numClassesBG = 3  // Số lớp cho Blood Glucose

    val parameters = ModelParameters(
        W1 = List(testInput[0].size) { List(numHiddenUnits) { 0.1 } },
        b1 = List(numHiddenUnits) { 0.1 },
        W2_bp = List(numHiddenUnits) { List(numClassesBP) { 0.1 } },
        b2_bp = List(numClassesBP) { 0.1 },
        W2_bg = List(numHiddenUnits) { List(numClassesBG) { 0.1 } },
        b2_bg = List(numClassesBG) { 0.1 }
    )

    // Forward propagation
    val forwardCache = forwardPropagationMultiOutput(testInput, parameters)

    // Display the results
    println("Forward Cache:")
    println("Z1: ${forwardCache.Z1}")
    println("A1: ${forwardCache.A1}")
    println("Z2_BP: ${forwardCache.Z2_bp}")
    println("A2_BP: ${forwardCache.A2_bp}")
    println("Z2_BG: ${forwardCache.Z2_bg}")
    println("A2_BG: ${forwardCache.A2_bg}")

    // Make predictions
    val predictions = predict(forwardCache.A2_bg)

    // Display predictions
    println("Predictions: $predictions")
}

