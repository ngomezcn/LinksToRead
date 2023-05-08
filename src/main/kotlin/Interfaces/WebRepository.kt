package Interfaces

import Models.WebModel

interface WebRepository{
    fun list() : List<WebModel>
    fun insert(item: WebModel)
    fun update(item: WebModel)
}
