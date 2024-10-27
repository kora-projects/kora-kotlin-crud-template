package ru.tinkoff.kora.kotlin.crud.model

import ru.tinkoff.kora.database.common.annotation.Column
import ru.tinkoff.kora.database.common.annotation.Id
import ru.tinkoff.kora.database.common.annotation.Table
import ru.tinkoff.kora.database.jdbc.EntityJdbc

@EntityJdbc
@Table("pets")
data class Pet(
    @field:Column("id") @field:Id val id: Long,
    @field:Column("name") val name: String,
    @field:Column("status") val status: Status
) {
    enum class Status(val code: Int) {
        AVAILABLE(0),
        PENDING(10),
        SOLD(20)
    }
}
