import Database.WebDAO
import Database.WebTable
import Models.Web
import kotlinx.serialization.Serializable
import java.awt.Desktop
import java.net.URI
import java.util.*
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.writeText

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class DataStorage
{
    var tempList = mutableListOf<Web>()

    var storageType = StorageType.SQL

    lateinit var db: Database
    val path = Paths.get("").toAbsolutePath().toString()+"_storage.json"

    init {
        setupStorage()
    }

    fun changeStorage(id : Int)
    {
        storageType = StorageType.fromId(id)!!
        setupStorage()
    }

    fun setupStorage()
    {
        when(storageType)
        {
            StorageType.FILE -> initFile()
            StorageType.MEMORY -> initMemory()
            StorageType.SQL -> initSQL()
        }
    }

    private fun initSQL()
    {
        db = Database.connect(
            "jdbc:postgresql://localhost:5432/postgres",
            driver = "com.impossibl.postgres.jdbc.PGDriver",
            user = "postgres",
            password = "1234"
        )
        initExposed()
    }

    private fun initMemory()
    {
        db = Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")
        initExposed()
    }

    private fun initExposed() {

        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(WebTable)
        }

        var query : MutableList<WebDAO>

        transaction {

            query = WebDAO.all().toMutableList()

            val temp : MutableList<Web> = mutableListOf()
            for(i in query)
            {
                val z = Web(i.name, i.link, i.read)
                temp.add(z)
            }
            tempList = temp
        }
    }

    private fun initFile() {
        println(path)

        var file = File(path)
        var fileExists = file.exists()

        if(fileExists){
            val text : String = Path(path).readText()
            val lines : List<String> = Path(path).readLines()

            tempList = Json.decodeFromString(text)
        }
    }

    fun saveData()
    {
        when(storageType)
        {
            StorageType.FILE -> saveToFile()
            StorageType.MEMORY -> saveToMemory()
            StorageType.SQL -> saveToMemory()
        }
    }

    private fun saveToFile()
    {
        val json = Json.encodeToString(tempList)

        val path = Path(path)
        path.writeText(json)
    }

    private fun saveToMemory()
    {
        transaction {
            WebDAO.all().forEach { it.delete() }
        }

        transaction {
            for(i in tempList)
            {
                val movie = WebDAO.new {
                    name = i.name
                    link = i.link
                    read = i.read
                }
            }
        }
    }
}

class Menu() {

    private val scanner = Scanner(System.`in`)
    private val dataStorage = DataStorage()

    fun start()
    {
        mainMenu()
    }

    fun openUrlInBrowser(url: String) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(URI(url))
            } catch (e: Exception) {
                println("Error al abrir el enlace: $url")
            }
        } else {
            println("No se puede abrir el enlace en el navegador.")
        }
    }

    private fun mainMenu()
    {
        while (true)
        {
            clearConsole()
            println("== Menú principal == ")
            println("1.- Añadir web")
            println("2.- Lista de webs")
            println("3.- Abrir web")
            println("4.- Cambiar tipo de guardado")
            println("5.- Cerrar")
            print("> ")
            when(scanner.nextLine().toInt())
            {
                1 -> addWeb()
                2 -> {listWebs(); enter()}
                3 -> {openWeb(); enter()}
                4 -> changeStorageType()
                5 -> return
            }
            dataStorage.saveData()
        }
    }

    private fun addWeb()
    {
        println("Añadir web")

        print("Name: ")
        val name = scanner.nextLine()

        print("Link: ")
        val link = scanner.nextLine()

        dataStorage.tempList.add(Web(name,link))
    }

    private fun listWebs()
    {
        println("Listando webs")
        for(i in dataStorage.tempList.indices)
        {
            println("$i.- ${dataStorage.tempList[i].name} - ${dataStorage.tempList[i].link} - Is readed: ${dataStorage.tempList[i].read}")
        }
    }

    private fun openWeb()
    {
        listWebs()
        println("Indica el num de la web para abrir")
        val id = scanner.nextLine().toInt()
        openUrlInBrowser(dataStorage.tempList[id].link)
        dataStorage.tempList[id].read = true
        println("Web abierta")
    }

    private fun changeStorageType()
    {
        println("Seleccione el método de guardado")

        println("1.- File")
        println("2.- Memory")
        println("3.- SQL")

        val id = scanner.nextLine().toInt()
        dataStorage.changeStorage(id)
    }
}