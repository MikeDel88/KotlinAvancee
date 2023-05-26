package androidkotlin.formation.kotlinavancee

data class Utilisateur(val name: String, val age: Int) {
    companion object {
        fun createBob() = Utilisateur("Bob", 10)
    }
}
