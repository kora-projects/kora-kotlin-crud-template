package ru.tinkoff.kora.example.controller

import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.example.model.PetMapper
import ru.tinkoff.kora.example.openapi.http.server.api.PetApiDelegate
import ru.tinkoff.kora.example.openapi.http.server.api.PetApiResponses
import ru.tinkoff.kora.example.openapi.http.server.model.MessageTO
import ru.tinkoff.kora.example.openapi.http.server.model.PetCreateTO
import ru.tinkoff.kora.example.openapi.http.server.model.PetUpdateTO
import ru.tinkoff.kora.example.service.PetService

@Component
class PetDelegate(
    private val petService: PetService,
    private val petMapper: PetMapper
) : PetApiDelegate {

    override fun getPetById(id: Long): PetApiResponses.GetPetByIdApiResponse {
        val pet = petService.findByID(id)
        if (pet != null) {
            val body = petMapper.asDTO(pet)
            return PetApiResponses.GetPetByIdApiResponse.GetPetById200ApiResponse(body)
        } else {
            return PetApiResponses.GetPetByIdApiResponse.GetPetById404ApiResponse(notFound(id))
        }
    }

    override fun addPet(petCreateTO: PetCreateTO): PetApiResponses.AddPetApiResponse {
        val pet = petService.add(petCreateTO)
        val body = petMapper.asDTO(pet)
        return PetApiResponses.AddPetApiResponse.AddPet200ApiResponse(body)
    }

    override fun updatePet(id: Long, petUpdateTO: PetUpdateTO): PetApiResponses.UpdatePetApiResponse {
        val updated = petService.update(id, petUpdateTO)
        if (updated != null) {
            val body = petMapper.asDTO(updated)
            return PetApiResponses.UpdatePetApiResponse.UpdatePet200ApiResponse(body)
        } else {
            return PetApiResponses.UpdatePetApiResponse.UpdatePet404ApiResponse(notFound(id))
        }
    }

    override fun deletePet(id: Long): PetApiResponses.DeletePetApiResponse {
        return if (petService.delete(id)) {
            PetApiResponses.DeletePetApiResponse.DeletePet200ApiResponse(
                MessageTO("Successfully deleted Pet with ID: $id")
            )
        } else {
            PetApiResponses.DeletePetApiResponse.DeletePet404ApiResponse(notFound(id))
        }
    }

    private fun notFound(petId: Long): MessageTO {
        return MessageTO("Pet not found for ID: $petId")
    }

    private fun malformedId(petId: Long): MessageTO {
        return MessageTO("Pet malformed ID: $petId")
    }
}