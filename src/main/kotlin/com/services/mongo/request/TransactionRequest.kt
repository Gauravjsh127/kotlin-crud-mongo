package com.services.mongo.request

import javax.validation.constraints.NotBlank

class TransactionRequest(
    @get:NotBlank val sender: String,
    @get:NotBlank val recipient: String,
    @get:NotBlank val amount: Float
)
