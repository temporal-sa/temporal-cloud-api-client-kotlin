package com.example.demo.controllers

import com.example.demo.client.TemporalCloudApiClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*

@RestController
class TestController {
    private val client: TemporalCloudApiClient = TemporalCloudApiClient("saas-api.tmprl.cloud", 443)

    @GetMapping("/create-namespace")
    public fun createNamespace(): String {
        val namespaceName = client.createNamespace("api-test", listOf("aws-us-west-2"), 1)
        return namespaceName
    }


    @GetMapping("/")
    public fun doit(): String {
        var html = "<h2>Users</h2>"
        val users = client.getUsers()
        html += "<table><tr><th>Id</th><th>Email</th><th>First Name</th><th>Created</th></tr>"
        for (user in users) {
            // timestamp to human readable
            val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(user.createdTime.seconds * 1000 + user.createdTime.nanos / 1000000))

            html += "<tr><td>${user.id}</td><td>${user.spec.email}</td>" +
                    "<td>${user.spec.access.accountAccess.role}</td>" +
                    "<td>${dateString}</td></tr>"
        }
        html += "</table>"

        html += "<br><br>"
        html += "<h2>Namespaces</h2>"
        val namespaces = client.getNamespaces()
        html += "<table><tr><th>Namespace</th><th>ID</th><th>Created</th></tr>"
        for (namespace in namespaces) {
            // timestamp to human readable
            val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(namespace.createdTime.seconds * 1000 + namespace.createdTime.nanos / 1000000))

            html += "<tr><td>${namespace.namespace}</td><td>${namespace.activeRegion}</td>" +
                    "<td>${dateString}</td></tr>"
        }

        return html
    }
}