package com.services.mongo.repository

import com.services.mongo.models.Transaction
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TransactionRepository : MongoRepository<Transaction, String> {
    fun findOneById(id: String): Transaction
    override fun deleteAll()
}
