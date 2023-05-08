import Interfaces.WebRepository
import Models.WebModel
import Repositories.InFileWebRepository
import Repositories.InMemoryWebRepository
import Repositories.InSQLWebRepository

class DataStorage
{
    var storageType = StorageType.SQL
    lateinit var repo : WebRepository

    object ServiceLocator {
        val fileRepository : WebRepository = InFileWebRepository()
        val memoryRepository : WebRepository = InMemoryWebRepository()
        val sqlRepository : WebRepository = InSQLWebRepository()
    }

    init {
        setupRepository()
    }

    fun changeStorage(id : Int)
    {
        storageType = StorageType.fromId(id)!!
        setupRepository()
    }

    fun setupRepository()
    {
        when(storageType)
        {
            StorageType.FILE -> {repo = ServiceLocator.fileRepository}
            StorageType.MEMORY ->{repo = ServiceLocator.memoryRepository}
            StorageType.SQL -> {repo = ServiceLocator.sqlRepository}
        }
    }

    fun insert(web : WebModel)
    {
        repo.insert(web)
    }

    fun list() : List<WebModel>
    {
        return repo.list()
    }

    fun update(webModel: WebModel) {
        repo.update(webModel)
    }
}