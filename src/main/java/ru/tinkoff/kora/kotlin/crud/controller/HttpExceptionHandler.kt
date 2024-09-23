package ru.tinkoff.kora.kotlin.example.crud.controller

import io.micrometer.core.instrument.config.validate.ValidationException
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.Context
import ru.tinkoff.kora.common.Tag
import ru.tinkoff.kora.http.common.body.HttpBody
import ru.tinkoff.kora.http.server.common.*
import ru.tinkoff.kora.json.common.JsonWriter
import ru.tinkoff.kora.kotlin.crud.openapi.http.server.model.MessageTO
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeoutException

@Tag(HttpServerModule::class)
@Component
class HttpExceptionHandler(private val errorJsonWriter: JsonWriter<MessageTO>) : HttpServerInterceptor {

    override fun intercept(
        context: Context,
        request: HttpServerRequest,
        chain: HttpServerInterceptor.InterceptChain
    ): CompletionStage<HttpServerResponse> {
        return chain.process(context, request).exceptionally { e ->
            if (e is HttpServerResponseException) {
                return@exceptionally e
            }

            val body = HttpBody.json(errorJsonWriter.toByteArrayUnchecked(MessageTO(e.message)))
            when (e) {
                is ValidationException -> HttpServerResponse.of(400, body)
                is IllegalArgumentException -> HttpServerResponse.of(400, body)
                is TimeoutException -> HttpServerResponse.of(408, body)
                else -> HttpServerResponse.of(500, body)
            }
        }
    }
}



