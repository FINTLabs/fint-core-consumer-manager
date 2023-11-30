package no.fintlabs.consumer.manager.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import kotlin.Exception

@Service
class ConsumerService {

    companion object {
        private val log = LoggerFactory.getLogger(ConsumerService::class.java)
    }

    fun getSpringVersion(repoName: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        try {
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject("https://raw.githubusercontent.com/FINTLabs/$repoName/main/build.gradle", String::class.java)

            result?.let {
                if (it.contains("org.springframework.boot' version")) {
                    it.split("\n").forEach { line ->
                        if (line.contains("org.springframework.boot' version")) {
                            map[repoName] = line.trim().replace("id 'org.springframework.boot' version", "")
                            return map
                        }
                    }
                }
            }
        } catch (e: Exception) {
            log.info("error:", e)
        }
        return map
    }

    val repoMap: MutableMap<String, List<String>> = mutableMapOf()
    fun getRepoTopic(repoName: String): Map<String, List<String>> {
        try {
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject("https://api.github.com/repos/FINTLabs/$repoName/topics", String::class.java)

            result?.let {
                val topicsMap = it.toTopicsMap()

                val topicsList = topicsMap["names"] as? List<String>

                if (topicsList != null && ("core" in topicsList || "consumer" in topicsList)) {
                    // Filter the topics to only include "core" and "consumer"
                    val filteredTopics = topicsList.filter { topic -> topic == "core" || topic == "consumer" }

                    repoMap[repoName] = filteredTopics
                    return repoMap
                }
            }
        } catch (e: Exception) {
            log.error("Error:", e)
        }

        return emptyMap()
    }

    // Extension function to convert JSON string to Map
    private fun String.toTopicsMap(): Map<*, *> {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(this, Map::class.java)
    }
}

