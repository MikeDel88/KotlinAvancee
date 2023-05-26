package androidkotlin.formation.kotlinavancee

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidkotlin.formation.kotlinavancee.utils.isVisible
import androidkotlin.formation.kotlinavancee.utils.toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

enum class Direction(val description: String) {
    NORD("Nord") {
        override fun action(): String  = "marcher"
    },
    EST("Est") {
        override fun action(): String  = "courir"
    },
    OUEST("Ouest") {
        override fun action(): String  = "sauter"
    },
    SUD("Sud") {
        override fun action(): String  = "nager"
    };

    abstract fun action(): String

    override fun toString(): String {
        return "$name : description=$description, action=${action()}"
    }
}
fun filterInts(numbers: Array<Int>, param: (Int) -> Boolean): Array<Int> {
    val filteredNumbers = mutableListOf<Int>()

    for(n in numbers) {
        if(param(n)) {
            filteredNumbers.add(n)
        }
    }

    return filteredNumbers.toTypedArray()
}
fun positiveInt(n: Int): Boolean = n > 0
fun evenInt(n: Int): Boolean = n % 2 == 0
fun arrayAction(array: Array<Int>, action: (Int) -> Unit) {
    for(a in array) {
        action(a)
    }
}
class DivideException(message: String, cause: Exception): Exception(message, cause)
fun divide(numerator: Int?, denominator: Int?): Int {
    try {
        return numerator!! / denominator!!
    } catch (e: ArithmeticException) {
        throw DivideException("Division par zéro interdite", e)
    } catch (e: NullPointerException) {
        throw DivideException("Opérande null", e)
    }
}
fun validateName(name:String) {
    require(name.isNotEmpty()) { "Empty name" }
    for(character in name)
        require(character.isLetter()) { "Invalid name, non letter character=$character"}
}
fun sendGift(user: User) {
    require(user.email.isNotEmpty()) { "Empty email" }
    check(user.state == User.State.ACTIVE) { "Invalid User State: ${user.state}" }
    println("sending gift to $user...")
}
data class User(var name: String = "", var email: String = "") {

    enum class State {
        NEW,
        ACTIVE
    }

    val state: State = State.NEW
    init {
        validateName(name)
    }
}
fun Utilisateur.canPlayBasketball() = this.age > 10
fun Utilisateur.Companion.createBobette() = Utilisateur("Bobette", 4)



class MainActivity : AppCompatActivity(), RedFragment.RedFragmentListener,
    GreenFragment.GreenFragmentListener {
    private val helloWorldLazy: TextView by lazy {
        println("lazy init TextView")
        findViewById(R.id.hello_world)
    }
    private val PERMISSION_CALL_PHONE = 0

    private val labelPlayer: TextView by lazy {
        println("lazy init TextView")
        findViewById(R.id.label_player)
    }
    private lateinit var helloWorldLate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        formationEnumeration(Direction.OUEST)
//        formationSealedClass()
//        formationHighOrderFunctions()
//        formationFonctionLambdas()
//        formationException()
//        formationPrediction()
//        formationElvisOperator()
//        formationCollectionsList()
//        formationCollectionSet()
//        formationCollectionsMap()
//        println(helloWorldLazy.text);formationLazyInit()
//        formationConst()
//        formationExtensionsFonctions()
//        formationExtensionsFonctionsLet()
//        formationExtensionsFonctionsApply()
//        formationExtensionsFonctionsWith()
//        formationExtensionsFonctionsRun()
//        formationExtensionsFonctionUse()
//        formationTraductioni18n()
//        formationCreationFragment()
//        formationPermission()
//        formationProgressBar()
//        formationSwipeRefresh()
//        formationHttpRetrofit()
//        formationDataBase()

    }

    /**
     * Enumerateur, Class qui définie des types dont tout est connu à l'avance.
     * Permet un typage Fort.
     */
    private fun formationEnumeration(ventDirection: Direction) {
        val action = when(ventDirection) {
            Direction.NORD -> "marcher"
            Direction.EST -> "courir"
            Direction.SUD -> "sauter"
            Direction.OUEST -> "nager"
        }

        println("Vent du ${ventDirection.description}, action=$action")
        println("Vent du ${ventDirection.description}, action=${ventDirection.action()}")

        val name: String = ventDirection.name
        val ordinal: Int = ventDirection.ordinal

        // Correspond au nom déclaré de la variable et sa position dans la liste des enums.
        println("$ventDirection : name=$name, ordinal=$ordinal")

        // On récupère les valeurs depuis le nom ou l'ordinal
        val directionFromName: Direction = Direction.valueOf(name)
        val directionFromOrdinal: Direction = Direction.values()[ordinal]

        println("Direction depuis nom : $directionFromName")
        println("Direction depuis ordinal : $directionFromOrdinal")

        // itération
        for(direction in Direction.values()) {
            println(direction)
        }
    }

    /**
     * Ensemble restreint de hérarchie de classe
     * Abstraite + membre abstraits
     * Nombre de classe enfants fixe et déclaré dans le même fichier.
     * Le type est explicite + Maitrise de la hiérarchie des classes.
     */
    private fun formationSealedClass() {

        var age = execute(10, Operation.Add(2))
        println("Addition: age=$age")

        age = execute(age, Operation.Substract(1))
        println("Addition: age=$age")

        age = execute(age, Operation.Increment)
        println("Addition: age=$age")

        age = execute(age, Operation.Decrement)
        println("Addition: age=$age")
    }

    /**
     * Fontion de première ordre (manipulable comme des variables)
     * Permet de passer des fonctions en paramètre.
     * filterInts(numbers, ::positiveInt) < on passe la référence vers la fonction
     */
    private fun formationHighOrderFunctions() {
        val numbers = arrayOf(-99,50,-6,-8,52,-56,1)

        val positiveNumber = filterInts(numbers, ::positiveInt)
        println("Nombres positifs: ${positiveNumber.contentToString()}")

        val evenNumber = filterInts(numbers, ::evenInt)
        println("Nombres pairs: ${evenNumber.contentToString()}")

        // On chaine les fonctions (on filtre d'abord les positifs puis les nombres pairs)
        val positiveEvenNumvers = filterInts(
            filterInts(numbers, ::positiveInt),
            ::evenInt
        )
        println("Nombres positifs & pairs: ${positiveEvenNumvers.contentToString()}")
    }

    /**
     * Fonction anonyme (pas de nom)
     * Directement utilisable (param et corps avec { p1, p2 -> body }
     * Dernière expression vaut "return"
     */
    private fun formationFonctionLambdas() {
        val numbers = arrayOf(-99,50,-6,-8,52,-56,1,119,5,-698)

        // Différente façon d'écrire une lambda
        numbers.filter { number -> number > 0 }
        numbers.filter { it > 0 }
        // Si lambda dernier paramètre d'une fonction.
        arrayAction(numbers) { number -> println(number) }
        numbers.forEach { println(it) }
        numbers.forEachIndexed { index, number -> println("Index: $index, Number: $number") }

        val button = Button(this)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                println("click")
            }
        })
        // fonction onClick anonyme.
        button.setOnClickListener { view -> println("click") }
        button.setOnClickListener { println("click") }
    }

    /**
     * gestion des erreurs, court-circuite le fonctionnement du programme
     * On a des classes qui hérite de Exception pour savoir laquelle on souhaite traiter.
     * NullPointerException || ClassCastException || IllegalStateException
     */
    private fun formationException() {
        val numerator: Int? = null
        val denominator = 2

        try {
            divide(numerator, denominator)
        } catch (e: DivideException) {
            println("${e.message}, case: ${e.cause}")
        } finally {
            println("Fin de l'opération")
        }

    }

    /**
     * Spécifier un invariants d'une fonction.
     * Gérer les erreurs des fonctions utilitaires.
     * require(IllegalArgument)
     * check(IllegalState)
     */
    private fun formationPrediction() {
        validateName("Bob")
        val bobette = User("B0b3tt3", "bobette@kotlin.com")
        sendGift(bobette)
    }

    /**
     * version courte if/else sur une ligne (test si valeur null et renvoi valeur par défaut)
     * Peut renvoyer une exception
     */
    private fun formationElvisOperator() {
        val name: String? = "Bob"

        val size = name?.length ?: throw IllegalArgumentException("Invalid name")
    }

    /**
     * formation Collections
     * Types : List, Set, Map
     * Vérification Mutable ou Immutable en fonction du type
     * couplé avec des lambdas et fonctions utilistaires.
     * (ArrayList / LinkedList...)
     */
    private fun formationCollectionsList() {
        val numbers = mutableListOf<Int>(1,2,3)
        numbers.add(42)
        numbers.add(8)

        val roNumbers: List<Int> = numbers
        val evenNumbers = numbers.filter { it % 2 == 0 }

        val names = listOf("Bob", null, "Bobette", null, "Mike")

        val longName = names.filterNotNull()
            .filter { it.length > 3 }
            .map { it.uppercase() }

        val containsLetterM = names.take(3)
            .filterNotNull()
            .any { it.lowercase().contains("m") }
    }

    /**
     * Liste d'éléments non ordonnées et unique.
     * Tester présence mais non récupérer.
     */
    private fun formationCollectionSet() {
        val uniqueNames = mutableSetOf("Bob", "Bobette")

        uniqueNames.add("John")
        uniqueNames.add("Bob")
        uniqueNames.add("Jane")
        println("Bob est-il présent ? ${uniqueNames.contains("Bob")}")

        val roNames: Set<String> = uniqueNames
        println("Element à l'indice 0 : ${roNames.elementAt(0)}") // pas fiable, ne sert qu'a connaître la présence.

       val list: List<String> = roNames.filter { it.startsWith("J") }
           .sorted()

    }

    /**
     * Stocker des listes de pair <clé, valeur>
     */
    private fun formationCollectionsMap() {
        val language = mutableMapOf("Kotlin" to "Un super langage",
            "Java" to "Un langage qui a fait son temps"
        )
        language.put("C++", "Une des origines du Java")
        if(language.contains("Python"))
            println("Il manque Python")

        for(key in language.keys) {
            println("Key: $key")
        }
        for(value in language.values) {
            println("Valeur: $value")
        }
        for(entry in language.entries) {
            println("Key : ${entry.key} => Valeur: ${entry.value}")
        }
        for((key, value) in language) {
            println("Key : $key ===> Valeur: $value")
        }

        val nonCppLanguages = language
            .filterNot { it.key == "C++" }
            .mapValues { it.value.uppercase() }
    }

    /**
     * Permet d'initialiser une variable la première fois qu'on y accède.
     */
    private fun formationLazyInit() {

        // Lambda appelé une seule fois.
        val language: String by lazy {
            println("Lazy init!")
            "Kotlin"
        }

        println(language)
        println(helloWorldLazy.text)
        helloWorldLate = findViewById(R.id.hello_world)

        // Utiliser lateinit pour point d'entrée unique (create) dans les activities.
        // Utiliser lazy module centraux initialiser une fois.
    }

    /**
     * Extension de val avec un niveau antérieur (définie à la compilation)
     * un val est définie à l'execution et const à la compilation.
     * défini hors d'une classe ou dans un companion object
     * type primitif ou String (pas d'instance de classe)
     */
    private fun formationConst() {
        //const val TEST: String = "test"
        //const val VERSION: Int = 42
    }

    /**
     * Fonction d'extension sur des types déjà existant..
     */
    private fun formationExtensionsFonctions() {

        if(helloWorldLazy.visibility == View.VISIBLE)
            Toast.makeText(this, "Hello world est visible", Toast.LENGTH_SHORT).show()

        if(helloWorldLazy.isVisible())
            toast("Hello world est visible")

        val bob = Utilisateur("Bob", 10)
        println("${bob.name} peut-il jouer au basket ? ${bob.canPlayBasketball()}")

        val bobette = Utilisateur.createBobette()
        println("${bobette.name} peut-il jouer au basket ? ${bobette.canPlayBasketball()}")

    }

    /**
     * Restreinte la portée ou scope d'un objet à un bloc
     * lambda en paramètre qu'il exécutera
     */
    private fun formationExtensionsFonctionsLet() {
        // instance de file
        File("config").let { file ->
            if(file.exists())
                println("$file existe")
            else
                file.createNewFile()
            println("$file fichier crée")
        }

        // remplace le if(file != null) pour faire quelque chose.
        val file: File? = null
        file?.let {
            file.createNewFile()
        }
    }

    /**
     * Permet d'éxecuter un lambda et renvoie l'objet.
     */
    private fun formationExtensionsFonctionsApply() {
        val longUser = User()
        longUser.name = "Bob"
        longUser.email = "bob@kotlin.com"
        println(longUser)

        val shortUser = User().apply {
            name = "Bobette"
            email = "bobette@kotlin.com"
        }
        println(shortUser)

        // au lieu d'écrire une fonctin createDirectory qui ferait 3 lignes.
        val shortHome = File("shortHome").apply { mkdir() }
    }

    /**
     * Permet de simplifier les appels multiples de plusieurs fonction sur un objet.
     */
    private fun formationExtensionsFonctionsWith() {
        val paint = Paint()
        paint.alpha = 100
        paint.color = Color.RED
        paint.strokeWidth = 2.0f

        with(paint) {
            alpha = 100
            color = Color.BLUE
            strokeWidth = 1.0f
        }
    }

    /**
     * Combo de let + with
     * renvoi dernière instruction du lambda
     */
    private fun formationExtensionsFonctionsRun() {
        val sb = StringBuilder()
        sb.append("Hello")
        sb.append("Kotlin")

        val sentence = sb.toString()
        println(sentence)

        val shortSentence = StringBuilder().run {
            append("Hello")
            append("run() rocks")
            toString() // dernière instruction renvoyé.
        }
    }

    /**
     * Permet automatiquement fermer un objet qui implemente Closeable en fin de lambda
     * Gère si on arrive ou ouvrir ou fermer un objet Closeable
     * Ce qui peut se fermer (FileReader / Cursor / Socket...)
     */
    private fun formationExtensionsFonctionUse() {

        val properties = Properties()
        with(properties) {
            setProperty("name", "Bob")
            setProperty("age", "10")
        }
        val file = File("config.properties")

        // Java
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(file)
            properties.store(fileOutputStream, null)
        } catch (e: IOException) {

        } finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close()
                } catch (e: IOException) {

                }
            }
        }

        // Kotlin
        FileOutputStream(file).use { _fileOutputStream ->
            properties.store(_fileOutputStream, null)
        }
        val loadedPropes = Properties().apply {
            FileInputStream(file).use { load(it) }
        }
    }

    /**
     * Formation sur les traduction possible
     * values (anglais) values-fr (français)
     * Gestion du pluriel avec les quantités.
     */
    private fun formationTraductioni18n() {
        // 1er paramètre correspond au qualifier (one, many)
        // 2ème pour remplacer l'argument dans le texte (quantité %d)
        labelPlayer.text = resources.getQuantityString(R.plurals.label_player, 2, 2)

        // Pour la partie arabe
        // Dans manifeste voir support RTL = true
        // xml : faire du EndOf au lieu de RightOf pour placer après plutôt que à droite de.
    }

    /**
     * Creation manuelle d'un fragment + rattachement.
     */
    private fun formationCreationFragment() {
        val greenFragment = GreenFragment()
       greenFragment.listener = this

        // Chargement du fragment.
        supportFragmentManager.beginTransaction()
            .add(R.id.container, greenFragment)
            .commit()
    }
    override fun onClick() {
        val blueFragment = BlueFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, blueFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN) //animation
            .addToBackStack("") // stock contenu de la transaction pour le bouton back.
            .commit()
    }

    /**
     * Formation sur les permissions normal (internet...)
     * Sur les permissions dangereuses (Contact, Appel...)
     * Demande de vérification depuis API 23 (attention targetSdk est majeur pour les permissions).
     */
    private fun formationPermission() {

        // Permission normale
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo?.isConnectedOrConnecting ?: false
        println("Connecté à internet : $isConnected")

        findViewById<Button>(R.id.call_phone).setOnClickListener {

            // On vérifie si on a la permission
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Si on ne l'a pas, on la requiert.
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), PERMISSION_CALL_PHONE)
            } else
                callPhone()

        }
    }
    private fun callPhone() {
        val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:12345"))
        startActivity(callIntent)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
           PERMISSION_CALL_PHONE -> {
               // On regarde si on a la permission.
               if(grantResults.isNotEmpty()
                   && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   callPhone()
               }
           }
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Formation sur la progession d'une progressBar horizontale.
     */
    private fun formationProgressBar() {
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        val buttonProgressBar = findViewById<Button>(R.id.progress_button)

        progressBar.max = 100
        progressBar.progress = 25
        progressBar.secondaryProgress = 30

        buttonProgressBar.setOnClickListener {
            progressBar.incrementProgressBy(5)
            progressBar.incrementSecondaryProgressBy(3)
        }
    }

    /**
     * Formation sur le swipe refresh qui permet de mettre à jour les données.
     */
    private fun formationSwipeRefresh() {
        val refresh = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh)

        refresh.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({ refresh.isRefreshing = false }, 2000)
        }
    }

    /**
     * Service Http qui permet de faire des requêtes et de convertir avec les datas class.
     */
    private fun formationHttpRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://httpbin.org")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        val service: HttpBinServiceString = retrofit.create(HttpBinServiceString::class.java)
        val call: Call<String> = service.getUserAgent()
        call.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.i("Retrofit", "User agent : ${response.body()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("Retrofit", t.message.toString())
            }

        })


        val retrofitJson = Retrofit.Builder()
            .baseUrl("https://httpbin.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val serviceJson: HttpBinServiceJson = retrofitJson.create(HttpBinServiceJson::class.java)
        val callJson: Call<GetData> = serviceJson.getInfoUser()
        callJson.enqueue(object: Callback<GetData> {
            override fun onResponse(call: Call<GetData>, response: Response<GetData>) {
                val getData = response.body()
                Log.i("Retrofit JSON url : ", "URL : ${getData?.url}")
            }

            override fun onFailure(call: Call<GetData>, t: Throwable) {
                //
            }

        })

    }


    private fun formationDataBase() {
        val database = DataBase(this)
        if(database.getUsersCount() == 0) {
            database.createUser(UserDB("Bob", 10, "bob@kotlin.com"))
            database.createUser(UserDB("Bobette", 4, "Bobette@kotlin.com"))
            database.createUser(UserDB("Mike", 15, "Mike@kotlin.com"))
            database.createUser(UserDB("John", 17, "John@kotlin.com"))
        }

        val users = database.getAllUsers()
        for(user in users) {
            Log.i("UserDB", "UserDB : $user")
        }
    }


}
