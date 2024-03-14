package no.fintlabs.consumer.manager.consumer

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import kotlin.Exception

@Service
class ConsumerService {

    companion object {
        private val log = LoggerFactory.getLogger(ConsumerService::class.java)
    }

    val repos = mutableListOf<String>()

    fun getFintVersion(): Map<String, String>{
        val map = mutableMapOf<String, String>()
        for(repo in repos){
            try {
                val restTemplate = RestTemplate()
                val result = restTemplate.getForObject(
                    "https://raw.githubusercontent.com/FINTLabs/${repo}/main/gradle.properties",
                    String::class.java
                )
                result?.let {
                    if (it.contains("version")) {
                        it.split("\n").forEach{line ->
                            if (line.contains("version"))
                                map[repo] = line
                        }
                    }
                }
            }catch (e: Exception){
                log.info("Error: ", e)
            }
        }
        return map
    }

    fun getSpringVersion(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        for (repo in repos){
        try {
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject(
                "https://raw.githubusercontent.com/FINTLabs/${repo}/main/build.gradle",
                String::class.java
            )
            result?.let {
                if (it.contains("org.springframework.boot' version")) {
                    it.split("\n").forEach { line ->
                        if (line.contains("org.springframework.boot' version")) {
                            map[repo] = line.trim().replace("id 'org.springframework.boot' version", "")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            log.info("error:", e)
            }
        }
        return map
    }

    @PostConstruct
    fun getConsumerRepos(): List<String> {
        try {
            val restTemplate = RestTemplate()
            val result = restTemplate.getForObject("https://api.github.com/search/repositories?q=org:Fintlabs+topic:core+topic:consumer", String::class.java)

            result?.let {
                it.split(",").forEach{line ->
                    if (line.contains("full_name")){
                        repos.add(line.substring(22).replace("\"", ""))
                        log.info("Filling cache ")
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
