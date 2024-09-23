package ru.tinkoff.kora.kotlin.example.crud.model

import org.mapstruct.Mapper
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.CategoryTO
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetTO

@Mapper
interface PetMapper {

    fun petWithCategoryToPetTO(pet: PetWithCategory): PetTO

    fun petCategoryToCategoryTO(category: PetCategory): CategoryTO
}
