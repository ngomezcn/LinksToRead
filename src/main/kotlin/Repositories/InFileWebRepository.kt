package Repositories

import Interfaces.WebRepository
import Models.WebModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.writeText

class InFileWebRepository : WebRepository {
    private val path = Paths.get("").toAbsolutePath().toString()+"_storage.json"
    private val file = File(path)

    override fun list(): List<WebModel> {
        val fileExists = file.exists()

        var tempList = mutableListOf<WebModel>()

        if(fileExists){
            val text : String = Path(path).readText()

            tempList = Json.decodeFromString(text)
        }
        return tempList
    }

    override fun insert(item: WebModel) {
        val fileExists = file.exists()

        var tempList = mutableListOf<WebModel>()

        if(fileExists){
            val text : String = Path(path).readText()

            tempList = Json.decodeFromString(text)
        }
        tempList.add(item)

        Path(path).writeText(Json.encodeToString(tempList))
    }

    override fun update(item: WebModel) {

        val fileExists = file.exists()

        var tempList = mutableListOf<WebModel>()

        if(fileExists){
            val text : String = Path(path).readText()

            tempList = Json.decodeFromString(text)
        }

        val index = tempList.indexOfFirst { it.hashCode() == item.hashCode() }
        if (index != -1) {
            tempList[index] = item
        }

        Path(path).writeText(Json.encodeToString(tempList))
    }
}