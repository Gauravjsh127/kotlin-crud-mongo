package com.services.mongo

import com.services.mongo.data.TransactionDto
import com.services.mongo.models.Transaction
import com.services.mongo.repository.TransactionRepository
import org.junit.jupiter.api.Assertions.* // ktlint-disable no-wildcard-imports
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionControllerItTest @Autowired constructor(
    private val transactionRepository: TransactionRepository,
    private val restTemplate: TestRestTemplate
) {
    private val defaultTransactionId = "default_transaction_id"

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        transactionRepository.deleteAll()
    }

    private fun getRootUrl(): String? = "http://localhost:$port/transactions"

    private fun saveOneTransaction() = transactionRepository.save(Transaction(defaultTransactionId, "test_1", "test_2", 420.toFloat()))

    private fun preparetransactionRequest() = TransactionDto("test_1", "test_2", 420.toFloat())

    @Test
    fun `should return all transaction`() {
        saveOneTransaction()

        val response = restTemplate.getForEntity(
            getRootUrl(),
            List::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `should return single transaction by id`() {
        saveOneTransaction()

        val response = restTemplate.getForEntity(
            getRootUrl() + "/$defaultTransactionId",
            Transaction::class.java
        )

        assertEquals(200, response.statusCode.value())
        assertNotNull(response.body)
        assertEquals(defaultTransactionId, response.body?.id)
    }

    // delete Transaction
    @Test
    fun `should delete existing transaction`() {
        saveOneTransaction()

        val delete = restTemplate.exchange(
            getRootUrl() + "/$defaultTransactionId",
            HttpMethod.DELETE,
            HttpEntity(null, HttpHeaders()),
            ResponseEntity::class.java
        )

        assertEquals(204, delete.statusCode.value())
        assertThrows(EmptyResultDataAccessException::class.java) { transactionRepository.findOneById(defaultTransactionId) }
    }
    // update operation
    @Test
    fun `should update existing transaction`() {
        saveOneTransaction()
        val transactionRequest = preparetransactionRequest()

        val updateResponse = restTemplate.exchange(
            getRootUrl() + "/$defaultTransactionId",
            HttpMethod.PUT,
            HttpEntity(transactionRequest, HttpHeaders()),
            Transaction::class.java
        )
        val updatedTask = transactionRepository.findOneById(defaultTransactionId)

        assertEquals(200, updateResponse.statusCode.value())
        assertEquals(defaultTransactionId, updatedTask.id)
        assertEquals(transactionRequest.sender, updatedTask.sender)
        assertEquals(transactionRequest.recipient, updatedTask.recipient)
    }

    @Test
    fun `should create new transaction`() {
        val transactionRequest = preparetransactionRequest()

        val response = restTemplate.postForEntity(
            getRootUrl(),
            transactionRequest,
            Transaction::class.java
        )

        assertEquals(201, response.statusCode.value())
        assertNotNull(response.body)
        assertNotNull(response.body?.id)
        assertEquals(transactionRequest.sender, response.body?.sender)
        assertEquals(transactionRequest.recipient, response.body?.recipient)
    }
}
