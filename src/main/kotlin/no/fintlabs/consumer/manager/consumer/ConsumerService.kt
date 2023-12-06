package no.fintlabs.consumer.manager.consumer

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


    val repos = mutableListOf<String>()
    fun getConsumerRepos(): List<String> {
        try {
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject("https://api.github.com/search/repositories?q=org:Fintlabs+topic:core+topic:consumer", String::class.java)

            result?.let {
                it.split(",").forEach{line ->
                    if (line.contains("full_name")){
                        repos.add(line.substring(22).replace("\"", ""))
                    }
                }
            }
            return repos
        } catch (e: Exception) {
            log.error("Error:", e)
        }

        return emptyList()
    }
}

