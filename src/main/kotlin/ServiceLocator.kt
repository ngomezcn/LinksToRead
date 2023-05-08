import Interfaces.WebRepository
import Repositories.InMemoryWebRepository

class ServiceLocator {
    val webRepository : WebRepository = InMemoryWebRepository()
}

