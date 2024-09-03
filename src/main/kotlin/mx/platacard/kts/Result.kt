package mx.platacard.kts

sealed class Result {
    data object Success : Result()

    data class Failure(
        val formattedMessage: String,
        val errors: List<Throwable> = emptyList(),
    ) : Result()
}
