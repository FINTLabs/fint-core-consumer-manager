package no.fintlabs.consumer.manager.gradle

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gradle")
class GradleController(val gradleService: GradleService) {

    @GetMapping("/version")
    fun getGradleVersion(): ResponseEntity<String> {
        return ResponseEntity.ok(gradleService.getGradleVersion())
    }

}