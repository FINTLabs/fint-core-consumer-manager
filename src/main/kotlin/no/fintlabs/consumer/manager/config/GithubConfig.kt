package no.fintlabs.consumer.manager.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "fint.github")
class GithubConfig {
    lateinit var token: String
    var username: String = "FINTLabs"
}