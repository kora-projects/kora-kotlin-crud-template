package ru.tinkoff.kora.example.service

import ru.tinkoff.kora.cache.annotation.CacheInvalidate
import ru.tinkoff.kora.cache.annotation.CachePut
import ru.tinkoff.kora.cache.annotation.Cacheable
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.example.model.Pet
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetCreateTO
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.PetUpdateTO
import ru.tinkoff.kora.kotlin.crud.repository.PetRepository
import ru.tinkoff.kora.resilient.circuitbreaker.annotation.CircuitBreaker
import ru.tinkoff.kora.resilient.retry.annotation.Retry
import ru.tinkoff.kora.resilient.timeout.annotation.Timeout

@Component
open class PetService(
    private val petRepository: PetRepository
) {

    @Cacheable(PetCache::class)
    @CircuitBreaker("pet")
    @Retry("pet")
    @Timeout("pet")
    open fun findByID(petId: Long): ru.tinkoff.kora.example.model.Pet? {
        return petRepository.findById(petId)
    }

    @CircuitBreaker("pet")
    @Timeout("pet")
    open fun add(createTO: PetCreateTO): ru.tinkoff.kora.example.model.Pet {
        val pet =
            ru.tinkoff.kora.example.model.Pet(0, createTO.name, ru.tinkoff.kora.example.model.Pet.Status.AVAILABLE)
        val petId = petRepository.insert(pet)
        return ru.tinkoff.kora.example.model.Pet(petId.toLong(), pet.name, pet.status)
    }

    @CircuitBreaker("pet")
    @Timeout("pet")
    @CachePut(value = PetCache::class, parameters = ["id"])
    open fun update(id: Long, updateTO: PetUpdateTO): ru.tinkoff.kora.example.model.Pet? {
        val existing = petRepository.findById(id) ?: return null

        if (existing.name == updateTO.name
            && updateTO.status != null
            && existing.status == toStatus(updateTO.status)
        ) {
            return existing
        }

        val status = if (updateTO.status == null) existing.status else toStatus(updateTO.status)
        val result = ru.tinkoff.kora.example.model.Pet(existing.id, updateTO.name, status)
        petRepository.update(result)
        return result
    }

    @CircuitBreaker("pet")
    @Timeout("pet")
    @CacheInvalidate(PetCache::class)
    open fun delete(petId: Long): Boolean {
        return petRepository.deleteById(petId).value() == 1L
    }

    companion object {
        private fun toStatus(statusEnum: PetUpdateTO.StatusEnum): ru.tinkoff.kora.example.model.Pet.Status {
            return when (statusEnum) {
                PetUpdateTO.StatusEnum.AVAILABLE -> ru.tinkoff.kora.example.model.Pet.Status.AVAILABLE
                PetUpdateTO.StatusEnum.PENDING -> ru.tinkoff.kora.example.model.Pet.Status.PENDING
                PetUpdateTO.StatusEnum.SOLD -> ru.tinkoff.kora.example.model.Pet.Status.SOLD
            }
        }
    }
}
