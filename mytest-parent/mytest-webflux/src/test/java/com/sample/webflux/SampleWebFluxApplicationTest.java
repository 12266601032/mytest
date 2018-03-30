package com.sample.webflux;

import org.junit.Test;
import org.junit.runner.RunWith;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Basic integration tests for WebFlux application.
 *
 * @author Brian Clozel
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SampleWebFluxApplicationTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    public void testWelcome() {
        this.webClient.get().uri("/").accept(MediaType.TEXT_PLAIN).exchange()
                .expectBody(String.class).isEqualTo("Hello World");
    }

    @Test
    public void testEcho() {
        this.webClient.post().uri("/echo").contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.TEXT_PLAIN)
                .body(Mono.just("Hello WebFlux!"), String.class).exchange()
                .expectBody(String.class).isEqualTo("Hello WebFlux!");
    }

    @Test
    public void testActuatorStatus() {
        this.webClient.get().uri("/actuator/health").accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk().expectBody()
                .json("{\"status\":\"UP\"}");
    }

}