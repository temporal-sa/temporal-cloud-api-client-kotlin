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
import temporal.api.cloud.cloudservice.v1.RequestResponse.CreateNamespaceRequest
import temporal.api.cloud.cloudservice.v1.RequestResponse.CreateNamespaceResponse
import temporal.api.cloud.namespace.v1.Message
import temporal.api.cloud.namespace.v1.Message.Namespace
import temporal.api.cloud.namespace.v1.Message.MtlsAuthSpec

import temporal.api.cloud.cloudservice.v1.RequestResponse.SetUserNamespaceAccessRequest
import temporal.api.cloud.cloudservice.v1.RequestResponse.SetUserNamespaceAccessResponse
import temporal.api.cloud.identity.v1.Message.NamespaceAccess

import java.util.Base64
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

    fun getNamespaces(): List<Namespace> {
        val request: GetNamespacesRequest = GetNamespacesRequest.newBuilder().build()
        try {
            val namespacesResponse: GetNamespacesResponse = blockingStub.getNamespaces(request)
            for (namespace: Namespace in namespacesResponse.namespacesList) {
                println(namespace)
            }
            return namespacesResponse.namespacesList
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }

    fun createNamespace(
        name: String,
        regions: List<String>,
        retentionDays: Int,
        customSearchAttributes: Map<String, String> = emptyMap()
    ): String {
        val caCert = System.getenv("TEMPORAL_CA_CERT_CONTENTS") ?: ""
        val caCertBase64 = Base64.getEncoder().encodeToString(caCert.toByteArray())

        val namespaceSpec = Message.NamespaceSpec.newBuilder()
            .setName(name)
            .addAllRegions(regions)
            .setRetentionDays(retentionDays)
            .setMtlsAuth(MtlsAuthSpec.newBuilder().setAcceptedClientCa(caCertBase64))
            .putAllCustomSearchAttributes(customSearchAttributes)
            .build()

        val request = CreateNamespaceRequest.newBuilder().setSpec(namespaceSpec)
            .build()

        try {
            val response: CreateNamespaceResponse = blockingStub.createNamespace(request)
            val createdNamespace = response.namespace
            println("Created namespace: $createdNamespace")
            return createdNamespace
        } catch (e: Exception) {
            println("Error creating namespace: $e")
            throw e
        }
    }

    fun setUserNamespaceAccess(
        namespace: String,
        userId: String,
        permission: String,
        resourceVersion: String,
        asyncOperationId: String? = null
    ): SetUserNamespaceAccessResponse {
        val namespaceAccess = NamespaceAccess.newBuilder()
            .setPermission(permission)
            .build()

        val request = SetUserNamespaceAccessRequest.newBuilder()
            .setNamespace(namespace)
            .setUserId(userId)
            .setAccess(namespaceAccess)
            .setResourceVersion(resourceVersion)
            .apply {
                asyncOperationId?.let { setAsyncOperationId(it) }
            }
            .build()

        try {
            val response: SetUserNamespaceAccessResponse = blockingStub.setUserNamespaceAccess(request)
            println("User namespace access set successfully")
            return response
        } catch (e: Exception) {
            println("Error setting user namespace access: $e")
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