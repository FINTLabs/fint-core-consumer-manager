package no.fintlabs.consumer.manager.gradle

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.fintlabs.consumer.manager.consumer.ConsumerService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import kotlin.math.log

@Service
class GradleService {

    @Value("\${fint.gradle.base-url:https://services.gradle.org/versions/current}")
    private lateinit var baseUrl: String

    private val restTemplate: RestTemplate = RestTemplate()

    fun getLatestGradleVersion(): String {
        val response = restTemplate.getForObject(baseUrl, String::class.java)
        val jsonNode = jacksonObjectMapper().readTree(response)
        return jsonNode.path("version").asText()
    }

    fun getGradleVersions(repo:String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        try {
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject(
                "https://raw.githubusercontent.com/FINTLabs/${repo}" +
                        "/main/gradle/wrapper/gradle-wrapper.properties", String::class.java
            )
            result.let {
                result?.split("\n")?.forEach {line ->
                    if (line.contains("distributionUrl")) {
                        val version = line.subSequence(66, 69).toString()
                        map.put(repo, version)
                    }
                }
            }
            return map
        } catch (e: Exception) {
            print(e)
        }
        return emptyMap();
    }
}