package com.examples

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

class KMySQLContainer(image: String) : MySQLContainer<KMySQLContainer>(image)

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MainTestIT {
    @Autowired
    private lateinit var testRepository: TestRepository
    @Autowired
    private lateinit var webTestClient: WebTestClient

    companion object {
        @Container
        val container = KMySQLContainer("mysql:latest").apply {
            withDatabaseName("foo")
            withUsername("foo")
            withPassword("secret")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl)
            registry.add("spring.datasource.password", container::getPassword)
            registry.add("spring.datasource.username", container::getUsername)
        }
    }

    @Test
    fun testFirst() {
        assertTrue(container.isRunning)
    }

    @Test
    fun testInsertAndGetEverything() {
        this.testRepository.save(MyEntity(1, "hello", "world"))
        this.testRepository.save(MyEntity(2, "hello again", "world again"))

        this.webTestClient.get()
                .uri("/all")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful
                .expectBody().jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("hello")
                .jsonPath("$[0].title").isEqualTo("world")
    }
}
