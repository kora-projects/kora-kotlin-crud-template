package ru.tinkoff.kora.kotlin.crud.service

import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import ru.tinkoff.kora.kotlin.crud.model.Pet

@Cache("pet-cache")
interface PetCache : CaffeineCache<Long, Pet>
