package no.fintlabs.consumer.manager.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ConsumerService {

    public Map<String, String> getSpringVersion(String repoName) {
        Map map = new HashMap<>();
        try {
            RestClient restClient = RestClient.create();
            String result = restClient.get()
                    .uri("https://raw.githubusercontent.com/FINTLabs/" + repoName + "/main/build.gradle")
                    .retrieve()
                    .body(String.class);

            if (result.contains("org.springframework.boot' version")){
                String[] lines = result.split("\n");
                for (String line: lines) {
                    if (line.contains("org.springframework.boot' version")){
                        map.put(repoName,line.strip().replace("id 'org.springframework.boot' version", ""));
                        return map;
                    }
                }
            }

            return map;
        }
        catch (Exception e) {
            log.info("error:", e);
        }
        return map;
    }
}