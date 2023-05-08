package Repositories

import Database.WebDAO
import Database.WebTable
import Interfaces.WebRepository
import Models.WebModel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.writeText

class InMemoryWebRepository : WebRepository{
    private val items = mutableListOf<WebModel>()

    override fun list(): List<WebModel> = items.toList()

    override fun insert(item: WebModel) {
        items.add(item)
    }

    override fun update(item: WebModel) {
        val index = items.indexOfFirst { it.hashCode() == item.hashCode() }
        if (index != -1) {
            items[index] = item
        }
    }
}

