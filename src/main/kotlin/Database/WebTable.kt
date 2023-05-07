package Database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object WebTable : IntIdTable() {
    val name: Column<String> = varchar("name", 50)
    val link: Column<String> = varchar("link", 50)
    val read: Column<Boolean> = bool("read")
}

class WebDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WebDAO>(WebTable)

    var name by WebTable.name
    var link by WebTable.link
    var read by WebTable.read
}