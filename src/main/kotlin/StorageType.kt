enum class StorageType(val id: Int) {
    FILE(1),
    MEMORY(2),
    SQL(2);

    companion object {
        fun fromId(id: Int): StorageType? {
            return values().find { it.id == id }
        }
    }
}