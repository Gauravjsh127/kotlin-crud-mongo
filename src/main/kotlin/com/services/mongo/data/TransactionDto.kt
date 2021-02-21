package com.services.mongo.data

import javax.validation.constraints.NotBlank

class TransactionDto(
    @get:NotBlank val sender: String,
    @get:NotBlank val recipient: String,
    @get:NotBlank val amount: Float
)
