package no.fintlabs.consumer.manager.consumer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class ConsumerController {

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping ("/repo/{repoName}")
    public ResponseEntity<Map<String, String>> getReposByName(@PathVariable String repoName){
        return ResponseEntity.ok(consumerService.getSpringVersion(repoName));
    }


}
