import com.example.issue1376routes
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Issue1376Test {
    val subject = issue1376routes

    @Test
    fun `non-spa routes defined before singlePageApp should match`() {
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
    fun `singlePageApp with a path prefix should not load outside the path prefix`() {
        val response = subject(Request(Method.GET, "/"))
        assertEquals(
            expected = Status.NOT_FOUND,
            actual = response.status,
        )
    }
}
