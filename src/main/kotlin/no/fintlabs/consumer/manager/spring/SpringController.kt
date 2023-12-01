package no.fintlabs.consumer.manager.spring

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/spring")
class SpringController(val springService: SpringService) {

    @GetMapping("/version")
    fun getSpringVersion(): ResponseEntity<String> {
        return ResponseEntity.ok(springService.getSpringVersion())
    }

}