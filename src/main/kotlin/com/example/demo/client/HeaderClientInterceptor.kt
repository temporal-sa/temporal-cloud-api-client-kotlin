package com.example.demo.client

import com.google.common.annotations.VisibleForTesting
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall
import io.grpc.ForwardingClientCallListener
import io.grpc.Metadata
import io.grpc.MethodDescriptor

class HeaderClientInterceptor(private val apiKey: String) : ClientInterceptor {
    companion object {
        private val AUTHORIZATION_HEADER_KEY: Metadata.Key<String> = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
        private val CUSTOM_HEADER_KEY: Metadata.Key<String> = Metadata.Key.of("temporal-cloud-api-version", Metadata.ASCII_STRING_MARSHALLER)
    }

    override fun <ReqT: Any, RespT: Any> interceptCall(
        method: MethodDescriptor<ReqT, RespT>,
        callOptions: CallOptions,
        next: Channel): ClientCall<ReqT, RespT> {
        return object: ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                headers.put(AUTHORIZATION_HEADER_KEY, "Bearer $apiKey")
                headers.put(CUSTOM_HEADER_KEY, "2024-05-13-00")
                super.start(object: ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    override fun onHeaders(headers: Metadata) {
                        super.onHeaders(headers)
                    }
                }, headers)
            }
        }
    }
}
