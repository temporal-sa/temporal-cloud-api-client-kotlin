package com.example.demo.controllers

import com.example.demo.client.TemporalCloudApiClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import temporal.api.cloud.cloudservice.v1.RequestResponse
import java.text.SimpleDateFormat
import temporal.api.cloud.identity.v1.Message.NamespaceAccess
import java.util.*
import org.springframework.web.bind.annotation.PathVariable

@RestController
class TestController {
    private val client: TemporalCloudApiClient = TemporalCloudApiClient("saas-api.tmprl.cloud", 443)

    @GetMapping("/create-namespace")
    public fun createNamespace(): String {
        val namespaceName = client.createNamespace("api-test", listOf("aws-us-west-2"), 1)
        return namespaceName
    }

    @GetMapping("/setUserNamespaceAccess/{namespace}/{userId}/{permission}/{resourceVersion}")
    fun setUserNamespaceAccess(
        @PathVariable namespace: String,
        @PathVariable userId: String,
        @PathVariable permission: String,
        @PathVariable resourceVersion: String
    ): String {
        val result = client.setUserNamespaceAccess(namespace, userId, permission, resourceVersion)
        return "$result<br/><br/><a href='/'>Back</a>"
    }

    @GetMapping("/")
    public fun doit(): String {

//        val result = client.setUserNamespaceAccess(
//            "steveandroulakis-test-1.sdvdw",
//            "7b439757f60044d3bee71ac2e338d532", "admin",
//            "410e9673-65c2-42d7-a836-9d4f906ec05f"
//        );
//        println(result.toString());

        // default namespace to change permissions on in testing
        val temporalNamespace = System.getenv("TEMPORAL_NAMESPACE") ?: "ERROR: TEMPORAL_NAMESPACE ENV VAR NOT SET"
        var html = "<h2>Users</h2>"
        val users = client.getUsers()
        html += "<table><tr><th>Id</th><th>Email</th><th>First Name</th><th>Resource Version</th><th>Created</th>" +
                "<th>on $temporalNamespace namespace</th>" +
                "</tr>"
        for (user in users) {
            // timestamp to human readable
            val dateString =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(user.createdTime.seconds * 1000 + user.createdTime.nanos / 1000000))

            html += "<tr><td>${user.id}</td><td>${user.spec.email}</td>" +
                    "<td>${user.spec.access.accountAccess.role}</td>" +
                    "<td>${user.resourceVersion}</td>" +
                    "<td>${dateString}</td>" +
                    "<td><a href=\"/setUserNamespaceAccess/$temporalNamespace/${user.id}/read/${user.resourceVersion}\">Set Read</a> -- " +
                    "<a href=\"/setUserNamespaceAccess/$temporalNamespace/${user.id}/write/${user.resourceVersion}\">Set Write</a> -- " +
                    "<a href=\"/setUserNamespaceAccess/$temporalNamespace/${user.id}/admin/${user.resourceVersion}\">Set Admin</a></td>" +
                    "</tr>"
        }
        html += "</table>"

        html += "<br><br>"
        html += "<h2>Namespaces</h2>"
        val namespaces = client.getNamespaces()
        html += "<table><tr><th>Namespace</th><th>ID</th><th>Created</th></tr>"
        for (namespace in namespaces) {
            // timestamp to human readable
            val dateString =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(namespace.createdTime.seconds * 1000 + namespace.createdTime.nanos / 1000000))

            html += "<tr><td>${namespace.namespace}</td><td>${namespace.activeRegion}</td>" +
                    "<td>${dateString}</td></tr>"
        }

        return html
    }
}