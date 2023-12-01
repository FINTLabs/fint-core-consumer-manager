package no.fintlabs.consumer.manager.gradle

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GradleService {

    @Value("\${fint.gradle.base-url:https://services.gradle.org/versions/current}")
    private lateinit var baseUrl: String

    private val restTemplate: RestTemplate = RestTemplate()

    fun getGradleVersion(): String {
        val response = restTemplate.getForObject(baseUrl, String::class.java)
        val jsonNode = jacksonObjectMapper().readTree(response)
        return jsonNode.path("version").asText()
    }

}