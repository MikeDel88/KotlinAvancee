package androidkotlin.formation.kotlinavancee

sealed class Operation {
    class Add(val value: Int): Operation()
    class Substract(val value: Int): Operation()
    // Pas de variable membres, donc on peut ecrire directement object.
    object Increment: Operation()
    object Decrement: Operation()
}

fun execute(x: Int, operation: Operation) = when(operation) {
    is Operation.Add -> x + operation.value
    is Operation.Substract -> x - operation.value
    Operation.Increment -> x + 1
    Operation.Decrement -> x - 1
}
