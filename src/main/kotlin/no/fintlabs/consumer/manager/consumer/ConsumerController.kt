package no.fintlabs.consumer.manager.consumer

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/consumer")
class ConsumerController(val consumerService: ConsumerService, val consumerUpdateService: ConsumerUpdateService) {

    @GetMapping("/")
    fun getRepo(): Map<String, String> {
        return consumerService.getSpringVersion()
    }

    @PostMapping("/update/spring")
    fun updateSpringBoot(@RequestBody request: Map<String, String>): ResponseEntity<Any> {
        return ResponseEntity.ok(consumerUpdateService.updateSpringBoot(request))
    }

}