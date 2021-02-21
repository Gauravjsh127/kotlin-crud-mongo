package com.services.mongo.services

import com.services.mongo.data.TransactionDto
import com.services.mongo.models.Transaction
import com.services.mongo.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception
import java.time.LocalDateTime

@Service
class TransactionService(val transactionRepository: TransactionRepository) {
    fun getTransaction(id: String): Transaction {
        return transactionRepository.findById(id).orElseThrow() {
            Exception("Unable to find transaction with id $id")
        }
    }

    fun getAllTransactions(): List<Transaction> = transactionRepository.findAll().toList()

    @Transactional
    fun updateTransaction(id: String, request: TransactionDto): Transaction {
        val transaction = transactionRepository.findOneById(id)
        return transactionRepository.save(
            Transaction(
                id = transaction.id,
                sender = request.sender,
                recipient = request.recipient,
                amount = request.amount,
                createdDate = transaction.createdDate,
                modifiedDate = LocalDateTime.now()
            )
        )
    }

    @Transactional
    fun createTransaction(request: TransactionDto): Transaction {
        return transactionRepository.save(
            Transaction(
                sender = request.sender,
                recipient = request.recipient,
                amount = request.amount
            )
        )
    }

    @Transactional
    fun deleteTransaction(id: String) {
        val transaction = getTransaction(id)
        transactionRepository.deleteById(id)
    }
}
