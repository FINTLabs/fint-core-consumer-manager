package no.fintlabs.consumer.manager.gradle

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.fintlabs.consumer.manager.consumer.ConsumerService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.function.Consumer
import kotlin.math.log

@Service
class GradleService(val consumerService: ConsumerService) {

    @Value("\${fint.gradle.base-url:https://services.gradle.org/versions/current}")
    private lateinit var baseUrl: String

    private val restTemplate: RestTemplate = RestTemplate()

    fun getLatestGradleVersion(): String {
        val response = restTemplate.getForObject(baseUrl, String::class.java)
        val jsonNode = jacksonObjectMapper().readTree(response)
        return jsonNode.path("version").asText()
    }

    fun getAllGradleVersionsInRepo(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val consumers = consumerService.getConsumerRepos()
        for (repo in consumers) {
            val gradleVersion = getGradleVersion(repo)
            map[repo] = gradleVersion
        }
        return map
    }

    fun getGradleVersion(repo: String): String {
        try {
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject(
                "https://raw.githubusercontent.com/FINTLabs/${repo}" +
                        "/main/gradle/wrapper/gradle-wrapper.properties", String::class.java
            )
            result.let {
                result?.split("\n")?.forEach { line ->
                    if (line.contains("distributionUrl")) {
                        val version = line.subSequence(66, 69).toString()
                        return version
                    }
                }
            }
        } catch (e: Exception) {
            print(e)
        }
        return ""
    }
}
