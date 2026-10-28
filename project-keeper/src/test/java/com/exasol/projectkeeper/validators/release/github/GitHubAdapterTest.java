package com.exasol.projectkeeper.validators.release.github;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.*;

import com.sun.net.httpserver.HttpServer;

class GitHubAdapterTest {
    private GitHubApiServer server;
    private GitHubAdapter adapter;

    @BeforeEach
    void startServer() throws IOException {
        server = new GitHubApiServer();
        server.start();
        adapter = new GitHubAdapter(new GitHubConnectionProvider(Map.of("GH_TOKEN", "test-token")), "exasol",
                "project-keeper", HttpClient.newHttpClient(), server.baseUri());
    }

    @AfterEach
    void stopServer() {
        server.close();
    }

    @Test
    void returnsOpenIssue() {
        assertThat(adapter.getIssueState(42), equalTo(IssueState.OPEN));
    }

    @Test
    void sendsBearerToken() {
        adapter.getIssueState(42);
        assertThat(server.authorization(), equalTo("Bearer test-token"));
    }

    @Test
    void returnsClosedIssue() {
        server.respond(200, "{\"state\":\"closed\"}");
        assertThat(adapter.getIssueState(42), equalTo(IssueState.CLOSED));
    }

    @Test
    void returnsMissingIssue() {
        server.respond(404, "");
        assertThat(adapter.getIssueState(42), equalTo(IssueState.MISSING));
    }

    @Test
    void wrapsFailedHttpResponse() {
        server.respond(500, "{\"message\":\"Internal Server Error\"}");
        final var exception = assertThrows(UncheckedIOException.class, () -> adapter.getIssueState(42));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-192: Failed to get status of GitHub issue #42: 'GitHub API returned HTTP status 500: {\"message\":\"Internal Server Error\"}'"));
    }

    @Test
    void wrapsMalformedJsonResponse() {
        server.respond(200, "<html>upstream error</html>");
        final var exception = assertThrows(UncheckedIOException.class, () -> adapter.getIssueState(42));
        assertThat(exception.getMessage(), equalTo(
                "E-PK-CORE-192: Failed to get status of GitHub issue #42: 'Failed to parse GitHub issue response as JSON: <html>upstream error</html>'"));
    }

    private static final class GitHubApiServer implements AutoCloseable {
        private final HttpServer server;
        private volatile int status = 200;
        private volatile String body = "{\"state\":\"open\"}";
        private final AtomicReference<String> authorization = new AtomicReference<>();

        private GitHubApiServer() throws IOException {
            this.server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            this.server.createContext("/repos/exasol/project-keeper/issues/42", exchange -> {
                this.authorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
                final byte[] response = this.body.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(this.status, response.length);
                try (var output = exchange.getResponseBody()) {
                    output.write(response);
                }
            });
        }

        private void start() {
            this.server.start();
        }

        private URI baseUri() {
            return URI.create("http://127.0.0.1:" + this.server.getAddress().getPort() + "/");
        }

        private void respond(final int status, final String body) {
            this.status = status;
            this.body = body;
        }

        private String authorization() {
            return this.authorization.get();
        }

        @Override
        public void close() {
            this.server.stop(0);
        }
    }
}
