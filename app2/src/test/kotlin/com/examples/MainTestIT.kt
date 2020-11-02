package com.examples

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class MainTestIT {

    companion object {
        @Container
        val container = MySQLContainer<Nothing>("mysql:latest").apply {
            withDatabaseName("foo")
            withUsername("foo")
            withPassword("secret")
        }
    }

    @Test
    fun test() {
        assertTrue(container.isRunning)
    }
}
