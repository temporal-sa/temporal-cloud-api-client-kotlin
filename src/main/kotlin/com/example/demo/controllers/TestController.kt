package com.example.demo.controllers

import com.example.demo.client.TemporalCloudApiClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*

@RestController
class TestController {
    private val client: TemporalCloudApiClient = TemporalCloudApiClient("saas-api.tmprl.cloud", 443)

    @GetMapping("/")
    public fun doit(): String {
        val users = client.getUsers()
        var html = "<table><tr><th>Id</th><th>Email</th><th>First Name</th><th>Created</th></tr>"
        for (user in users) {
            // timestamp to human readable
            val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(user.createdTime.seconds * 1000 + user.createdTime.nanos / 1000000))

            html += "<tr><td>${user.id}</td><td>${user.spec.email}</td>" +
                    "<td>${user.spec.access.accountAccess.role}</td>" +
                    "<td>${dateString}</td></tr>"
        }
        html += "</table>"
        return html
    }
}