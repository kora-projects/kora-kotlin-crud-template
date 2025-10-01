package ru.tinkoff.kora.example

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import ru.tinkoff.kora.example.openapi.http.server.model.PetCreateTO
import ru.tinkoff.kora.example.openapi.http.server.model.PetUpdateTO
import ru.tinkoff.kora.example.repository.PetRepository
import ru.tinkoff.kora.example.service.PetCache
import ru.tinkoff.kora.example.service.PetService
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest
import ru.tinkoff.kora.test.extension.junit5.KoraAppTestConfigModifier
import ru.tinkoff.kora.test.extension.junit5.KoraConfigModification
import ru.tinkoff.kora.test.extension.junit5.TestComponent

@KoraAppTest(Application::class)
class ComponentTests : KoraAppTestConfigModifier {

    @field:MockK
    @TestComponent
    private lateinit var petCache: PetCache

    @field:MockK
    @TestComponent
    private lateinit var petRepository: PetRepository

    @TestComponent
    private lateinit var petService: PetService

    override fun config(): KoraConfigModification = KoraConfigModification.ofString(
        """
           resilient {
              circuitbreaker.pet {
                slidingWindowSize = 2
                minimumRequiredCalls = 2
                failureRateThreshold = 100
                permittedCallsInHalfOpenState = 1
                waitDurationInOpenState = 15s
              }
              timeout.pet {
                duration = 5000ms
              }
              retry.pet {
                delay = 100ms
                attempts = 2
              }
            }
    """.trimIndent()
    )

    @Test
    fun updatePetWithNewCategoryCreated() {
        // given
        mockCache()
        mockRepository()

        val added = petService.add(PetCreateTO("dog"))
        assertEquals(1, added.id)
        verify { petRepository.insert(any()) }

        // when
        every { petRepository.findById(any()) } returns added
        val updated = petService.update(
            added.id,
            PetUpdateTO("cat", PetUpdateTO.StatusEnum.PENDING)
        )
        assertNotNull(updated)
        assertEquals(1, updated!!.id)

        // then
        verify { petRepository.update(any()) }
    }

    @Test
    fun updatePetWithSameCategory() {
        // given
        mockCache()
        mockRepository()

        val added = petService.add(PetCreateTO("dog"))
        assertEquals(1, added.id)
        verify { petRepository.insert(any()) }

        // when
        every { petRepository.findById(any()) } returns added
        val updated = petService.update(
            added.id,
            PetUpdateTO("cat", PetUpdateTO.StatusEnum.PENDING)
        )
        assertNotNull(updated)
        assertEquals(1, updated!!.id)

        // then
        verify { petRepository.update(any()) }
    }

    private fun mockCache() {
        every { petCache.get(any<Long>()) } returns null
        every { petCache.put(any<Long>(), any()) } returnsArgument (1)
    }

    private fun mockRepository() {
        every { petRepository.insert(any()) } returns 1
        every { petRepository.findById(any()) } returns null
        every { petRepository.update(any()) } returns Unit
    }
}
