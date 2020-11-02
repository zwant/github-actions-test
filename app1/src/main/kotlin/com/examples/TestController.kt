package com.examples

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(private val testRepository: TestRepository) {

    @GetMapping
    @RequestMapping("/all")
    fun getAllThings(): List<MyEntity> = testRepository.findAll()
}
