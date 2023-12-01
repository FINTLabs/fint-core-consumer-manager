package no.fintlabs.consumer.manager.spring

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SpringService {

    @Value("\${fint.spring.base-url:https://start.spring.io/actuator/info}")
    private lateinit var baseUrl: String

    private val restTemplate: RestTemplate = RestTemplate()

    fun getSpringVersion(): String {
        val response = restTemplate.getForObject(baseUrl, String::class.java)
        val jsonNode = jacksonObjectMapper().readTree(response)
        return jsonNode.path("build").path("versions").path("spring-boot").asText()
    }

}