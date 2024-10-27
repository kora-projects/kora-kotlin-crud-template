package ru.tinkoff.kora.kotlin.crud.model

import org.mapstruct.Mapper
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetTO

@Mapper
interface PetMapper {

    fun asDTO(pet: Pet): PetTO
}
