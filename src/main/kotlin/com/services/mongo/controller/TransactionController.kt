package com.services.mongo.controller

import com.services.mongo.models.Transaction
import com.services.mongo.services.TransactionService
import com.services.mongo.data.TransactionDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.* // ktlint-disable no-wildcard-imports
import java.time.LocalDateTime

@RestController
@RequestMapping("/transactions")
class TransactionController(
    val transactionService: TransactionService
) {

    @GetMapping
    fun getAllTransactions(): ResponseEntity<List<Transaction>> {
        return ResponseEntity.ok(transactionService.getAllTransactions())
    }

    @GetMapping("/{id}")
    fun getTransactionById(@PathVariable("id") id: String): ResponseEntity<Transaction> {
        val transaction = transactionService.getTransaction(id = id)
        return ResponseEntity.ok(transaction)
    }

    @PostMapping
    fun createTransaction(@RequestBody request: TransactionDto): ResponseEntity<Transaction> {
        val transaction = transactionService.createTransaction(request)
        return ResponseEntity(transaction, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updateTransaction(@RequestBody request: TransactionDto, @PathVariable("id") id: String): ResponseEntity<Transaction> {
        val updatedTransaction = transactionService.updateTransaction(id =id, request = request)
        return ResponseEntity.ok(updatedTransaction)
    }

    @DeleteMapping("/{id}")
    fun deleteTransactionById(@PathVariable("id") id: String): ResponseEntity<Unit> {
        transactionService.deleteTransaction(id =id)
        return ResponseEntity.noContent().build()
    }
}
