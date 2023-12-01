package no.fintlabs.consumer.manager.consumer

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ConsumerUpdateService {

    val springCache: MutableMap<String, String> = mutableMapOf()

    fun updateSpringBoot(repos: Map<String, String>): ResponseEntity<Any> {
        repos.forEach { (repo, version) ->
            springCache[repo] = version
            println("Updated $repo to $version")
        }
        return ResponseEntity.ok().build()
    }


}