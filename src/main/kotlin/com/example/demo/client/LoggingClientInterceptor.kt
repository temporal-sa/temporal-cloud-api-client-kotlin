package com.example.demo.client

import io.grpc.*
import io.grpc.Metadata
import java.util.logging.Logger

class LoggingClientInterceptor: ClientInterceptor {
    companion object {
        val logger: Logger = Logger.getLogger(LoggingClientInterceptor.javaClass.name)
    }
    override fun <ReqT: Any, RespT: Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<ReqT, RespT> {
        return next.newCall(method, callOptions)
    }
}