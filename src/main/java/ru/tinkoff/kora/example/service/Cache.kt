package ru.tinkoff.kora.example.service

import ru.tinkoff.kora.cache.annotation.Cache
import ru.tinkoff.kora.cache.caffeine.CaffeineCache
import ru.tinkoff.kora.example.model.Pet

@Cache("pet-cache")
interface PetCache : CaffeineCache<Long, ru.tinkoff.kora.example.model.Pet>
