package ru.tinkoff.kora.example.model

import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetTO
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetTO.StatusEnum

@Component
class PetMapper {

    fun asDTO(pet: ru.tinkoff.kora.example.model.Pet): PetTO {
        val enum: StatusEnum = when (pet.status) {
            ru.tinkoff.kora.example.model.Pet.Status.AVAILABLE -> StatusEnum.AVAILABLE
            ru.tinkoff.kora.example.model.Pet.Status.PENDING -> StatusEnum.PENDING
            ru.tinkoff.kora.example.model.Pet.Status.SOLD -> StatusEnum.SOLD
        }

        return PetTO(pet.id, pet.name, enum)
    }
}
