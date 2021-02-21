package com.services.mongo.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class HealthController() {

    @Operation(summary = "Health check API")
    @GetMapping("/health")
    fun getHealth(): String {
        return "OK"
    }
}
