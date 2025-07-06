package com.example

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp

val issue1376routes = routes(
    "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
    "/dashboard" bind singlePageApp(ResourceLoader.Classpath("dashboard")),
)
