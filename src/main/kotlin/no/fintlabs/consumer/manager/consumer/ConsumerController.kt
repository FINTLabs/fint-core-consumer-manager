package no.fintlabs.consumer.manager.consumer

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/consumer")
class ConsumerController(val consumerService: ConsumerService, val consumerUpdateService: ConsumerUpdateService) {

    @GetMapping("/{repoName}")
    fun getRepo(@PathVariable repoName: String): Map<String, String> {
        return consumerService.getSpringVersion(repoName)
    }

    @GetMapping("topic/{repoName}")
    fun getTopic(@PathVariable repoName: String): Map<String, List<String>>{
        return consumerService.getRepoTopic(repoName)
    }

    @PostMapping("/update/spring")
    fun updateSpringBoot(@RequestBody request: Map<String, String>): ResponseEntity<Any> {
        return consumerUpdateService.updateSpringBoot(request)
    }

}