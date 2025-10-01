package ru.tinkoff.kora.example.repository

import ru.tinkoff.kora.database.common.UpdateCount
import ru.tinkoff.kora.database.common.annotation.Id
import ru.tinkoff.kora.database.common.annotation.Query
import ru.tinkoff.kora.database.common.annotation.Repository
import ru.tinkoff.kora.database.jdbc.JdbcRepository
import ru.tinkoff.kora.example.model.Pet

@Repository
interface PetRepository : JdbcRepository {

    @Query("SELECT %{return#selects} FROM %{return#table} WHERE id = :id")
    fun findById(id: Long): Pet?

    @Id
    @Query("INSERT INTO %{entity#inserts -= id}")
    fun insert(entity: Pet): Int

    @Query("UPDATE %{entity#table} SET %{entity#updates} WHERE %{entity#where = @id}")
    fun update(entity: Pet)

    @Query("DELETE FROM pets WHERE id = :id")
    fun deleteById(id: Long): UpdateCount
}
