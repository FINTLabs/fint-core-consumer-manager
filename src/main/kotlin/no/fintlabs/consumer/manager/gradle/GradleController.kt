package no.fintlabs.consumer.manager.gradle

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/gradle")
class GradleController(val gradleService: GradleService) {

    @GetMapping("/{repo}")
    fun getGradleVersions(@PathVariable repo: String): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(gradleService.getGradleVersions(repo))
    }

    @GetMapping("/version")
    fun getLatestGradleVersion(): ResponseEntity<String> {
        return ResponseEntity.ok(gradleService.getLatestGradleVersion())
    }
}
