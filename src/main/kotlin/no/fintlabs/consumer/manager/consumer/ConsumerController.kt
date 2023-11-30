package no.fintlabs.consumer.manager.consumer

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ConsumerController(val consumerService: ConsumerService) {

    @GetMapping("/{repoName}")
    fun getRepo(@PathVariable repoName: String): Map<String, String> {
        return consumerService.getSpringVersion(repoName)
    }

    @GetMapping("topic/{repoName}")
    fun getTopic(@PathVariable repoName: String): Map<String, List<String>>{
        return consumerService.getRepoTopic(repoName)
    }
}