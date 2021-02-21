package com.services.mongo.controller

import com.services.mongo.models.Transaction
import com.services.mongo.repository.TransactionRepository
import com.services.mongo.data.TransactionDto
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports
import java.time.LocalDateTime

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val transactionRepository: TransactionRepository
) {

    @GetMapping
    fun getAllTransactions(): ResponseEntity<List<Transaction>> {
        val transactions = transactionRepository.findAll()
        return ResponseEntity.ok(transactions)
    }

    @GetMapping("/{id}")
    fun getTransactionById(@PathVariable("id") id: String): ResponseEntity<Transaction> {
        val transaction = transactionRepository.findOneById(ObjectId(id))
        return ResponseEntity.ok(transaction)
    }

    @PostMapping
    fun createTransaction(@RequestBody request: TransactionDto): ResponseEntity<Transaction> {
        val transaction = transactionRepository.save(
            Transaction(
                sender = request.sender,
                recipient = request.recipient,
                amount = request.amount
            )
        )
        return ResponseEntity(transaction, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateTransaction(@RequestBody request: TransactionDto, @PathVariable("id") id: String): ResponseEntity<Transaction> {
        val transaction = transactionRepository.findOneById(ObjectId(id))
        val updatedTransaction = transactionRepository.save(
            Transaction(
                id = transaction.id,
                sender = request.sender,
                recipient = request.recipient,
                amount = request.amount,
                createdDate = transaction.createdDate,
                modifiedDate = LocalDateTime.now()
            )
        )
        return ResponseEntity.ok(updatedTransaction)
    }

    @DeleteMapping("/{id}")
    fun deleteTransactionById(@PathVariable("id") id: String): ResponseEntity<Unit> {
        transactionRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
