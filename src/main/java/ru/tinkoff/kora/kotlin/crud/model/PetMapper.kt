package ru.tinkoff.kora.kotlin.crud.model

import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetTO
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetTO.StatusEnum

@Component
class PetMapper {

    fun asDTO(pet: Pet): PetTO {
        val enum: StatusEnum = when (pet.status) {
            Pet.Status.AVAILABLE -> StatusEnum.AVAILABLE
            Pet.Status.PENDING -> StatusEnum.PENDING
            Pet.Status.SOLD -> StatusEnum.SOLD
        }

        return PetTO(pet.id, pet.name, enum)
    }
}
