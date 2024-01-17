package com.example.demo.client

import com.google.api.Logging
import io.grpc.Channel
import io.grpc.ClientInterceptors
import io.grpc.ManagedChannelBuilder
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc.CloudServiceBlockingStub
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc.CloudServiceFutureStub
import temporal.api.cloud.cloudservice.v1.CloudServiceGrpc.CloudServiceStub
import temporal.api.cloud.cloudservice.v1.RequestResponse.GetNamespacesRequest
import temporal.api.cloud.cloudservice.v1.RequestResponse.GetNamespacesResponse
import temporal.api.cloud.namespace.v1.Message.Namespace
import java.util.logging.Logger

class TemporalCloudApiClient (host:String, port:Int) {
    private val logger: Logger = Logger.getLogger("foo")
    var blockingStub: CloudServiceBlockingStub

    init {
        val channel = ManagedChannelBuilder.forAddress(host, port)
            .build()
        val apiKey = System.getenv("TEMPORAL_CLOUD_API_KEY") ?: ""
        val headerInterceptor = HeaderClientInterceptor(apiKey)
        blockingStub = CloudServiceGrpc.newBlockingStub(ClientInterceptors.intercept(channel, headerInterceptor, LoggingClientInterceptor()))
    }

    fun getNamespaces(): Int {
        val request: GetNamespacesRequest = GetNamespacesRequest.newBuilder().build()
        try {
            val namespacesResponse: GetNamespacesResponse = blockingStub.getNamespaces(request)
            for (namespace: Namespace in namespacesResponse.namespacesList) {
                println(namespace)
            }
            return namespacesResponse.namespacesCount
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }

    fun getUsers(): List<temporal.api.cloud.identity.v1.Message.User> {
        val request = temporal.api.cloud.cloudservice.v1.RequestResponse.GetUsersRequest.newBuilder().build()
        try {
            val usersResponse = blockingStub.getUsers(request)
            for (user in usersResponse.usersList) {
                println(user)
            }
            return usersResponse.usersList
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }


}