package com.examples

import org.springframework.data.jpa.repository.JpaRepository

interface TestRepository : JpaRepository<MyEntity, Long>
