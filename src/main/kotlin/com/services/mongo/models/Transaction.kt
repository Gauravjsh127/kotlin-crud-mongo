package com.services.mongo.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Transaction(
    @Id
    val id: ObjectId = ObjectId.get(),
    val sender: String,
    val recipient: String,
    val amount: Float,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val modifiedDate: LocalDateTime = LocalDateTime.now()
)
