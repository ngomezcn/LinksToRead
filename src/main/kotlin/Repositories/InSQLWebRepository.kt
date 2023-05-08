package Repositories

import Database.WebDAO
import Database.WebTable
import Interfaces.WebRepository
import Models.WebModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class InSQLWebRepository : WebRepository {
    lateinit var db: Database

    override fun list(): List<WebModel> {
        var temp : MutableList<WebModel> = mutableListOf()

        transaction {

            val query : MutableList<WebDAO> = WebDAO.all().toMutableList()

            temp = mutableListOf()
            for(i in query)
            {
                val z = WebModel(i.name, i.link, i.read)
                temp.add(z)
            }
        }

        return temp.toList()
    }

    override fun insert(web: WebModel) {
        transaction {
            val movie = WebDAO.new {
                name = web.name
                link = web.link
                read = web.read
            }
        }
    }

    override fun update(web: WebModel) {
        transaction {
            val webDAO =  WebDAO.find { (WebTable.name eq web.name) and (WebTable.link eq web.link) }.firstOrNull()
            if (webDAO != null) {
                webDAO.name = web.name
                webDAO.link = web.link
                webDAO.read = web.read
            }
        }
    }

    init {
        db = Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(WebTable)
        }
    }
}