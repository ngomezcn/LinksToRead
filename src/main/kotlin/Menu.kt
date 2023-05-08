import Interfaces.WebRepository
import Models.WebModel
import Repositories.InFileWebRepository
import Repositories.InMemoryWebRepository
import Repositories.InSQLWebRepository
import java.awt.Desktop
import java.net.URI
import java.util.*

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
                1 -> {addWeb(); enter()}
                2 -> {listWebs(); enter()}
                3 -> {openWeb(); enter()}
                4 -> changeStorageType()
                5 -> return
            }
        }
    }

    private fun addWeb()
    {
        println("Añadir web")

        print("Name: ")
        val name = scanner.nextLine()

        print("Link: ")
        val link = scanner.nextLine()

        dataStorage.insert(WebModel(name, link, false))
    }

    private fun listWebs()
    {
        var index = 0
        println("Listando webs")
        for(i in dataStorage.list())
        {
            println("$index .- $${i.name} - ${i.link} - Is readed: ${i.read}")
            index++
        }
    }

    private fun openWeb()
    {
        listWebs()
        val webList = dataStorage.list()
        println("Indica el num de la web para abrir")
        val id = scanner.nextLine().toInt()
        openUrlInBrowser(webList[id].link)
        webList[id].read = true

        dataStorage.update(webList[id])

        println("Web abierta")
    }

    private fun changeStorageType()
    {
        println("Seleccione el método de guardado")
        println("Actual: ${dataStorage.storageType.name}")

        println("1.- File")
        println("2.- Memory")
        println("3.- SQL")

        val id = scanner.nextLine().toInt()
        dataStorage.changeStorage(id)
    }
}