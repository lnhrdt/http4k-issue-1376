import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.singlePageApp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Issue1376Test {
    @Test
    fun `non-spa routes defined before singlePageApp should match`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            "/dashboard" bind singlePageApp(ResourceLoader.Classpath("dashboard")),
        )
        val response = subject(Request(Method.GET, "/api/info"))
        assertEquals(
            expected = Status.OK,
            actual = response.status,
        )
        assertEquals(
            expected = "ISSUE 1376",
            actual = response.bodyString(),
        )
    }

    @Test
    fun `singlePageApp with a path prefix should load an index dot html, implicitly`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            "/dashboard" bind singlePageApp(ResourceLoader.Classpath("dashboard")),
        )
        val response = subject(Request(Method.GET, "/dashboard"))
        assertEquals(
            expected = Status.OK,
            actual = response.status,
        )
        assertTrue {
            response.bodyString()
                .contains("<h1>Hello, World!</h1>")
        }
    }

    @Test
    fun `singlePageApp with a path prefix should load an index dot html, explicitly`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            "/dashboard" bind singlePageApp(ResourceLoader.Classpath("dashboard")),
        )
        val response = subject(Request(Method.GET, "/dashboard/index.html"))
        assertEquals(
            expected = Status.OK,
            actual = response.status,
        )
        assertTrue {
            response.bodyString()
                .contains("<h1>Hello, World!</h1>")
        }
    }

    @Test
    fun `singlePageApp with a path prefix should load other resources, explicitly`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            "/dashboard" bind singlePageApp(ResourceLoader.Classpath("dashboard")),
        )
        val response = subject(Request(Method.GET, "/dashboard/main.css"))
        assertEquals(
            expected = Status.OK,
            actual = response.status,
        )
        assertTrue {
            response.bodyString()
                .contains("background-color: bisque;")
        }
    }

    @Test
    fun `singlePageApp with a path prefix should not load on the root path`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            "/dashboard" bind singlePageApp(ResourceLoader.Classpath("dashboard")),
        )
        val response = subject(Request(Method.GET, "/"))
        assertEquals(
            expected = Status.NOT_FOUND,
            actual = response.status,
        )
    }

    @Test
    fun `singlePageApp with a path prefix should not load on a different path`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            "/dashboard" bind singlePageApp(ResourceLoader.Classpath("dashboard")),
        )
        val response = subject(Request(Method.GET, "/pizza"))
        assertEquals(
            expected = Status.NOT_FOUND,
            actual = response.status,
        )
    }

    @Test
    fun `singlePageApp with a base path should load an index dot html, implicitly`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            singlePageApp(ResourceLoader.Classpath("dashboard")).withBasePath("/dashboard"),
        )
        val response = subject(Request(Method.GET, "/dashboard"))
        assertEquals(
            expected = Status.OK,
            actual = response.status,
        )
        assertTrue {
            response.bodyString()
                .contains("<h1>Hello, World!</h1>")
        }
    }

    @Test
    fun `singlePageApp with a base path should not load on the root path`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            singlePageApp(ResourceLoader.Classpath("dashboard")).withBasePath("/dashboard"),
        )
        val response = subject(Request(Method.GET, "/"))
        assertEquals(
            expected = Status.NOT_FOUND,
            actual = response.status,
        )
    }

    @Test
    fun `singlePageApp with a base path should not load on a different path`() {
        val subject = routes(
            "/api/info" bind Method.GET to { Response(Status.OK).body("ISSUE 1376") },
            singlePageApp(ResourceLoader.Classpath("dashboard")).withBasePath("/dashboard"),
        )
        val response = subject(Request(Method.GET, "/pizza"))
        assertEquals(
            expected = Status.NOT_FOUND,
            actual = response.status,
        )
    }
}
