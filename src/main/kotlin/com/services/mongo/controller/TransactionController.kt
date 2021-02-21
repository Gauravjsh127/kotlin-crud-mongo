package com.services.mongo.controller

import com.services.mongo.data.Transaction
import com.services.mongo.repository.TransactionRepository
import com.services.mongo.request.TransactionRequest
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
    fun getAllPatients(): ResponseEntity<List<Transaction>> {
        val patients = transactionRepository.findAll()
        return ResponseEntity.ok(patients)
    }

    @GetMapping("/{id}")
    fun getOnePatient(@PathVariable("id") id: String): ResponseEntity<Transaction> {
        val patient = transactionRepository.findOneById(ObjectId(id))
        return ResponseEntity.ok(patient)
    }

    @PostMapping
    fun createPatient(@RequestBody request: TransactionRequest): ResponseEntity<Transaction> {
        val patient = transactionRepository.save(
            Transaction(
                name = request.name,
                description = request.description
            )
        )
        return ResponseEntity(patient, HttpStatus.CREATED)
    }

    @PutMapping("/{id}")
    fun updatePatient(@RequestBody request: TransactionRequest, @PathVariable("id") id: String): ResponseEntity<Transaction> {
        val patient = transactionRepository.findOneById(ObjectId(id))
        val updatedPatient = transactionRepository.save(
            Transaction(
                id = patient.id,
                name = request.name,
                description = request.description,
                createdDate = patient.createdDate,
                modifiedDate = LocalDateTime.now()
            )
        )
        return ResponseEntity.ok(updatedPatient)
    }

    @DeleteMapping("/{id}")
    fun deletePatient(@PathVariable("id") id: String): ResponseEntity<Unit> {
        transactionRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}
